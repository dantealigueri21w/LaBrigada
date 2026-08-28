# Bitácora de compilación — La Brigada

## Parte 1: scaffolding y dominio

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **26 tests, 0 fallos** (23 de la ficha
  más 3 para `MotorProgreso.estaDesbloqueado`, la función de desbloqueo de la sección 5.1 v13
  del maestro).
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- Motores (`domain/engine/`): `MotorEscena` (7 tests, con reglas de distancia relativas entre
  objetos, no solo estado individual), `MotorRiesgoRestante` (5), `MotorSimulacro` (5),
  `MotorProgreso` (9: 6 de insignias/racha + 3 de desbloqueo).
- Ajuste sobre el manifiesto placeholder del plan: se reemplazó `Theme.Material3.DayNight.NoActionBar`
  (biblioteca no declarada) por un tema propio `Theme.LaBrigada` (padre
  `android:Theme.Material.Light.NoActionBar`) y se creó desde el paso 1 un ícono de lanzador
  adaptativo real (silueta de chaleco de brigada en el naranja de la ficha), en vez de dejar un
  mipmap inexistente. Detalle en `handoffs/INCIDENCIAS-60-LaBrigada.md`, I-01.
- Sin UI todavía, sin Room todavía.

## Parte 2: Room y datos semilla

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **49 tests, 0 fallos**.
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- Persistencia real con Room 2.8.4, probada con Robolectric 4.16.1 (`@Config(sdk = [34])`).
- 8 tablas (las 7 de la ficha + `repaso_pendiente`). Las reglas de distancia entre objetos
  (fogata/carpa, líquido inflamable/fuente de calor) se modelan con una columna
  `distanciaMinimaDeId` en `objeto_riesgo`, no con una tabla aparte.
- Datos semilla reales: 8 lugares, 36 objetos de riesgo, 11 insignias.
- `MotorRepaso` (6 tests) y 3 tests nuevos de `MotorProgreso.estaDesbloqueado` (regla de
  desbloqueo de la sección 5.1 v13, ver handoff sección 0.6.a).
- `BrigadaRepository` (7 tests): compone Room con `MotorEscena`, `MotorSimulacro` y
  `MotorProgreso` ya construidos en la Parte 1. El estado de arrastre en vivo (qué objeto ya
  está corregido) vive en la sesión de Compose de la Parte 3, no en Room.
- Dependencias de Room/Robolectric ya venían resueltas desde el scaffolding inicial (Parte 1),
  así que la Task 1 de este plan no requirió cambios adicionales.
- Sin UI todavía — eso es la Parte 3.
