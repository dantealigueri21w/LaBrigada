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
