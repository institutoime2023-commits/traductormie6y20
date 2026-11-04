package com.orvex.traductor.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.sqrt

/**
 * Captura bloques PCM16 temporales para calcular el nivel RMS.
 * No escribe archivos ni transmite audio.
 */
class MicrophoneManager(private val context: Context) {

    private val _level = MutableStateFlow(0f)
    val level: StateFlow<Float> = _level

    private var recorder: AudioRecord? = null
    private var captureJob: Job? = null
    private var scope: CoroutineScope? = null

    @Synchronized
    fun start(): Boolean {
        if (recorder?.recordingState == AudioRecord.RECORDSTATE_RECORDING) return true

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) return false

        val sampleRate = 16_000
        val minBuffer = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        if (minBuffer <= 0) return false

        val bufferSize = maxOf(minBuffer, 4096)
        val audioRecord = try {
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
        } catch (_: SecurityException) {
            return false
        } catch (_: IllegalArgumentException) {
            return false
        }

        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            audioRecord.release()
            return false
        }

        return try {
            audioRecord.startRecording()
            if (audioRecord.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                audioRecord.release()
                false
            } else {
                recorder = audioRecord
                val localScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
                scope = localScope
                captureJob = localScope.launch {
                    val buffer = ShortArray(2048)
                    while (isActive) {
                        val count = audioRecord.read(buffer, 0, buffer.size)
                        if (count > 0) {
                            var sum = 0.0
                            for (i in 0 until count) {
                                val sample = buffer[i].toDouble() / 32768.0
                                sum += sample * sample
                            }
                            val rms = sqrt(sum / count).toFloat()
                            // Escala visual suavizada; no representa un porcentaje clínico.
                            _level.value = (rms * 5.0f).coerceIn(0f, 1f)
                        } else if (count < 0) {
                            break
                        }
                    }
                    _level.value = 0f
                }
                true
            }
        } catch (_: Exception) {
            audioRecord.release()
            recorder = null
            false
        }
    }

    @Synchronized
    fun stop() {
        captureJob?.cancel()
        captureJob = null
        scope?.cancel()
        scope = null

        try {
            recorder?.let {
                if (it.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    it.stop()
                }
                it.release()
            }
        } catch (_: Exception) {
            // Liberación defensiva del recurso de audio.
        }
        recorder = null
        _level.value = 0f
    }
}
