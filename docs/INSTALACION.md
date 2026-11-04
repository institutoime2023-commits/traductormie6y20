# Instalación y primera prueba

## Android Studio

1. Descomprimir el ZIP.
2. Abrir la carpeta raíz del proyecto en Android Studio.
3. Aceptar la sincronización de Gradle.
4. Instalar Android SDK Platform 35 si Android Studio lo solicita.
5. Usar JDK 17 para Gradle.
6. Conectar el Redmi 12C y habilitar Opciones de desarrollador → Depuración USB.
7. Ejecutar la configuración `app`.

## Generar APK desde Android Studio

Menú **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

La APK debug se genera normalmente en:

`app/build/outputs/apk/debug/app-debug.apk`

Nota: el sufijo `.debug` se añade al identificador de la variante debug. El identificador de instalación de prueba será `com.orvex.traductor.debug`.

Para una compilación release, usa **Build → Generate Signed Bundle / APK** y crea una clave de firma propia. Conserva esa clave en un lugar seguro; será necesaria para actualizar la aplicación instalada.

## Prueba

- Abrir TRADUCTOR.
- Pulsar PROBAR MICRÓFONO.
- Conceder permiso.
- Hablar y observar el indicador.
- Pulsar DETENER MICRÓFONO.
- Verificar que Android muestre el permiso de micrófono concedido y que el indicador vuelva a cero al detener.
