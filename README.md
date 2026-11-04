# TRADUCTOR Android — V0.1

Aplicación Android nativa inicial para el producto TRADUCTOR.

- Nombre visible: **TRADUCTOR**
- Package name release: `com.orvex.traductor`
- Package name debug: `com.orvex.traductor.debug` (sufijo para instalar pruebas sin confundirlas con una futura versión comercial).
- Versión: `0.1.0` (`versionCode 1`)
- Plataforma: Android
- Lenguaje: Kotlin
- Interfaz: Jetpack Compose
- Prueba incluida: permiso y captura temporal del micrófono con indicador de nivel RMS.
- No incluye todavía Whisper, traducción automática, TTS, licencias ni conexión con n8n.

## Requisitos de desarrollo

- Android Studio compatible con Android Gradle Plugin 8.7.3.
- JDK 17.
- Android SDK Platform 35.
- Gradle 8.9 (Android Studio puede descargar la distribución al sincronizar).
- Conexión a Internet durante la primera sincronización para descargar dependencias.

## Abrir y compilar

1. Descomprime `TRADUCTOR-ANDROID-V0.1.zip`.
2. En Android Studio selecciona **Open** y elige la carpeta `TRADUCTOR-ANDROID-V0.1`.
3. Espera a que Gradle sincronice y descargue las dependencias.
4. Conecta el Redmi 12C con depuración USB habilitada, o inicia un emulador.
5. Pulsa **Run** para instalar la aplicación.

Si Android Studio pregunta por la versión de Gradle, utiliza Gradle 8.9 y JDK 17.

## Prueba de micrófono

1. Abre TRADUCTOR.
2. Pulsa **PROBAR MICRÓFONO**.
3. Acepta el permiso de grabación de audio.
4. Habla cerca del teléfono: el indicador debe reaccionar al nivel de sonido.
5. Pulsa **DETENER** para liberar el micrófono.

La aplicación lee temporalmente bloques PCM para calcular el nivel de audio. No guarda archivos, no transmite audio y no mantiene una grabación.

## Estructura

```text
app/src/main/
├── AndroidManifest.xml
├── java/com/orvex/traductor/
│   ├── MainActivity.kt
│   ├── audio/MicrophoneManager.kt
│   └── ui/theme/Theme.kt
└── res/
    ├── drawable/ic_launcher_foreground.xml
    └── values/
        ├── strings.xml
        └── themes.xml
```

## Límites conocidos de V0.1

- La captura se prueba únicamente mientras la pantalla de la aplicación está visible.
- No hay servicio de micrófono en segundo plano.
- No hay procesamiento de voz ni traducción todavía.
- No hay autenticación ni validación de suscripción.
- El audio capturado permanece en memoria únicamente durante el cálculo del nivel y se descarta.

## Próximas versiones previstas

- V0.2: evaluación e integración de reconocimiento de voz local.
- V0.3: traducción local inglés → español.
- V0.4: síntesis de voz local.
- V0.5: reproducción continua y Bluetooth.
- V0.6: cola, fragmentación y sincronización de audio.
- V0.7: usuarios, dispositivos y licencias.
