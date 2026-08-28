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

## Parte 3: tema, pantallas y navegación (arte SVG pendiente aparte)

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **79 tests, 0 fallos**.
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- `./gradlew assembleDebug`: BUILD SUCCESSFUL, APK de depuración real generado.
- Tema con contraste WCAG verificado: el Primario de la ficha (`#E8622C`) necesitó una variante
  oscurecida (`#BA4E23`, 5.00:1 con blanco) como único color de superficie con texto; el hex
  oficial se conserva para ilustraciones/iconos sin texto.
- Los 8 lugares comparten `LugarScreen`, una sola pantalla parametrizada por `lugarId`.
- Corrección real por arrastre (`ZonaSoltar`/`FichaArrastrable`, copiado del ARCHIVO real de
  Base de Campo, no del texto del plan de La Brigada, que traía el bug del offset contado dos
  veces sin corregir) + dos casos de toque directo para los objetos de conducta de La Calle,
  con halo visual propio (borde del color de advertencia) para que se vean distintos de un
  objeto arrastrable.
- Ningún objeto de riesgo arranca ya corregido: `LugarViewModel` inicia siempre `corregidos`
  vacío (sección 5.7). Soltar un objeto en la zona de otro no dispara nada porque cada
  `FichaArrastrable` compara solo contra su propia zona (sección 1.2 del handoff) — no hace
  falta lógica de "rebote contado", la arquitectura ya lo impide.
- **Desbloqueo (sección 5.1 v13):** `MotorProgreso.estaDesbloqueado` calculado, no una columna
  nueva en `LugarEntity`. Los 5 estados (`EstadoLugar`) se derivan de señales ya persistidas:
  "iniciado" = existe un intento fallido en `repaso_pendiente` para ese lugar; "dominado" =
  completado sin ningún intento fallido. El Home los muestra con icono Y texto (nunca solo
  color) y cada bloqueado dice cuántos lugares más hacen falta.
- **Perfil (sección 5.11):** pantalla propia (`PerfilScreen`), alcanzable desde el Home y
  DISTINTA del *parental gate* (el botón de perfil del Home navega a `Rutas.PERFIL`, nunca a
  `Rutas.PARENTAL_GATE` — el bug exacto de Base de Campo y Huellario). Alias con valor por
  defecto del mundo de la app ("Brigadista Firu"), 12 avatares elegibles
  (`SelectorDeAvatares`, mismo patrón verificado de La Plaza/Sin Grietas), elegidos al final del
  onboarding y editables después. `perfilDao` se llama desde `BrigadaRepository`, fuera de
  `data/` (`grep -rn "perfilDao" app/src/main/java --include="*.kt" | grep -v "/data/"` da
  resultado real).
- **Bitácora de la Brigada:** pantalla propia con el historial real de correcciones
  (`obtenerBitacora()`), no una lista de ejemplo.
- **Parental gate:** se accede con una pulsación larga de 3s sobre el título del Home (sección
  17), nunca desde el botón de perfil.
- `MainActivity` usa `LaunchedEffect` dentro de `setContent`, nunca `runBlocking` en
  `onCreate` (bug ya documentado en la sección 1.9 del archivo de lecciones).
- `LifecycleResumeEffect` en la ruta Home: sin él, volver de un lugar recién corregido no
  refresca el mapa del cuartel.
- **Arte:** por ahora, un dispatcher esquemático en Canvas (`ui/art/Arte.kt`,
  `ui/art/Avatares.kt`) mientras se hace el paso de SVG -> VectorDrawable (sección 4.0/4.1.5 del
  maestro). Documentado como pendiente explícito, no un placeholder silencioso: se completa
  antes de cerrar la Fase 1.
- [PENDIENTE antes de cerrar Fase 1: jugar un ciclo real en el emulador `fabrica34` — Paso 4 de
  la Task 9 del plan]
