# La Brigada

Aplicación móvil de una brigada infantil de prevención donde el niño corrige escenas
inseguras, para el aprendizaje práctico de la seguridad y la prevención de riesgos
cotidianos.

Nativa Android (Kotlin + Jetpack Compose), sin conexión a internet, con persistencia
local en Room.

## Cómo compilar

Requiere Android SDK (compileSdk 37, minSdk 24) y JDK 17.

```
./gradlew assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/`.

## Cómo correr los tests

```
./gradlew testDebugUnitTest
./gradlew lintDebug
```
