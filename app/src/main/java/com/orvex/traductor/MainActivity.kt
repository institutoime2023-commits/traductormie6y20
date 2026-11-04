package com.orvex.traductor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.orvex.traductor.audio.MicrophoneManager
import com.orvex.traductor.ui.theme.TraductorTheme
import kotlinx.coroutines.delay

private val Navy = Color(0xFF101827)
private val Panel = Color(0xFF1C2B3E)
private val Cyan = Color(0xFF20C8E8)
private val Muted = Color(0xFFA8BED4)

class MainActivity : ComponentActivity() {

    private lateinit var microphoneManager: MicrophoneManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        microphoneManager = MicrophoneManager(this)

        setContent {
            TraductorTheme {
                TranslatorHome(
                    microphoneManager = microphoneManager,
                    hasPermission = {
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        if (::microphoneManager.isInitialized) microphoneManager.stop()
        super.onDestroy()
    }
}

@androidx.compose.runtime.Composable
private fun TranslatorHome(
    microphoneManager: MicrophoneManager,
    hasPermission: () -> Boolean
) {
    var running by remember { mutableStateOf(false) }
    var level by remember { mutableFloatStateOf(0f) }
    var status by remember { mutableStateOf("Listo para probar") }

    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val started = microphoneManager.start()
            running = started
            status = if (started) {
                "Escuchando el micrófono del teléfono"
            } else {
                "No se pudo iniciar el micrófono"
            }
        } else {
            status = "Permiso de micrófono denegado"
        }
    }

    LaunchedEffect(running) {
        while (running) {
            level = microphoneManager.level.value
            delay(80)
        }
    }

    DisposableEffect(Unit) {
        onDispose { microphoneManager.stop() }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Navy
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = Cyan,
                    modifier = Modifier.size(34.dp)
                )
                Text(
                    text = "TRADUCTOR",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(Modifier.weight(1f))
                Text("V0.1", color = Cyan, fontWeight = FontWeight.SemiBold)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("IDIOMAS", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "ENGLISH     ⇄     ESPAÑOL",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Traducción local en desarrollo", color = Muted, fontSize = 12.sp)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("PRUEBA DEL MICRÓFONO", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(if (running) Color(0xFF35D07F) else Muted)
                        )
                    }
                    Spacer(Modifier.height(22.dp))
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Micrófono",
                        tint = if (running) Cyan else Muted,
                        modifier = Modifier.size(58.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        if (running) "MICRÓFONO ACTIVO" else "MICRÓFONO DETENIDO",
                        color = if (running) Color(0xFF35D07F) else Muted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(status, color = Muted, fontSize = 12.sp)
                    Spacer(Modifier.height(18.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF30445A))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(level.coerceIn(0f, 1f))
                                .height(10.dp)
                                .clip(CircleShape)
                                .background(Cyan)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Nivel de entrada de audio", color = Muted, fontSize = 11.sp)
                }
            }

            Button(
                onClick = {
                    if (running) {
                        microphoneManager.stop()
                        running = false
                        level = 0f
                        status = "Captura detenida"
                    } else if (hasPermission()) {
                        val started = microphoneManager.start()
                        running = started
                        status = if (started) "Escuchando el micrófono del teléfono" else "No se pudo iniciar el micrófono"
                    } else {
                        microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (running) Color(0xFFB23B48) else Color(0xFF087E9B)
                )
            ) {
                androidx.compose.material3.Icon(
                    imageVector = if (running) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = null
                )
                Text(
                    if (running) "DETENER MICRÓFONO" else "PROBAR MICRÓFONO",
                    modifier = Modifier.padding(start = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                "Esta versión solo comprueba la captura de audio. Todavía no traduce ni genera voz.",
                color = Muted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomItem("Ajustes", Icons.Default.Settings)
                BottomItem("Audio", Icons.Default.Headphones)
                BottomItem("Cuenta", Icons.Default.Language)
            }
            Text("ORVEX · TRADUCTOR V0.1", color = Color(0xFF6F849A), fontSize = 10.sp)
        }
    }
}

@androidx.compose.runtime.Composable
private fun BottomItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.material3.Icon(icon, contentDescription = null, tint = Cyan)
        Text(label, color = Muted, fontSize = 11.sp)
    }
}
