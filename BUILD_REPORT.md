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
## Arte real y limpieza del repositorio (sesión previa)

- Arte real SVG → VectorDrawable: 54 piezas (8 lugares, 22 objetos de riesgo agrupados por forma,
  11 insignias, 12 avatares, portada), generadas con
  `documentos-fuente/_scripts-generadores/gen_labrigada_vector.py`, miradas en Chrome y corregidas
  en 2 vueltas completas. `aapt2 compile` pasa sobre los 56 XML de `res/drawable/`.
- Limpieza de higiene: se retiraron del repositorio `arte-svg/`, `arte-xml/` (duplicado de
  staging), `hoja-contactos.html` y las capturas de referencia (`contactos_v2_*.png`,
  `check_recreo.png`) — material de proceso que nunca debió comitearse (sección 11/14.1). Commit
  de limpieza separado del trabajo de arte.

## Cierre de Fase 1: playtesting real + un bug encontrado y corregido

- Emulador `fabrica34` recuperado de un estado corrupto (toques no se registraban,
  `uiautomator dump` devolvía contenido de una app ya desinstalada) con `-wipe-data`. Confirmado
  limpio (`pm list packages -3` → 0 paquetes de terceros) antes de instalar el APK. Detalle en
  `handoffs/INCIDENCIAS-60-LaBrigada.md`, I-04.
- `./gradlew clean testDebugUnitTest lintDebug assembleDebug`: BUILD SUCCESSFUL, **81 tests, 0
  fallos** (79 + 2 nuevos de regresión sobre el bug de abajo), lint limpio.
- **Ciclo real completo jugado en el emulador**, con datos limpios (`pm clear` implícito por el
  `-wipe-data`), los 8 lugares de principio a fin, incluidos los dos casos que se corrigen
  **tocando** en La Calle (halo visual propio, `content-desc` distinto, decrementan el contador
  igual que un arrastre). Verificado con capturas y `uiautomator dump` en cada paso, no solo con
  los tests:
  - Ningún objeto arranca ya corregido en ningún lugar (sección 5.7).
  - Soltar un objeto en la zona segura equivocada rebota y NO cuenta como corregido (verificado
    con captura antes/después en Mi Cuarto).
  - El desbloqueo en cascada (`MotorProgreso.estaDesbloqueado`) funciona exacto con progreso real:
    3 abiertos al arrancar, cada lugar completado abre el siguiente según la fórmula
    `lugaresCompletados >= orden - 3`, y cada bloqueado muestra el número exacto que le falta.
  - Los 5 estados del Home se distinguen con icono (candado/triángulo/estrella), nunca solo color.
  - El botón "Pedir una pista a Firu" da una pista de una frase, específica del primer objeto
    pendiente, sin revelar la respuesta (sección 5.8).
  - Perfil (alias + avatar) y progreso sobreviven a un `force-stop` + relanzamiento real de la
    app, no solo a un `adb install -r` (sección 5.11).
- **Bug real encontrado jugando el ciclo completo, no por los tests:** `El Simulacro Final` (el
  8º lugar) nunca quedaba registrado como corregido — el Home se quedaba en "7 de 8" para
  siempre y la insignia "Brigada Completa" no se podía ganar. Causa: `evaluarSimulacroFinal` en
  `BrigadaRepository` nunca insertaba en `correccion_registrada` (solo en `simulacro_resultado`),
  y el test que en teoría cubría "Brigada Completa" probaba un camino de código
  (`registrarCorreccion` llamado directo para los 8 lugares) que la UI real nunca ejecuta para el
  simulacro. Corregido: `evaluarSimulacroFinal` ahora también registra la corrección y llama
  `actualizarProgreso()` cuando el simulacro pasa. Verificado jugando de nuevo tras el fix:
  "8 de 8 lugares corregidos", "El Simulacro Final, Dominado". Detalle completo en
  `handoffs/INCIDENCIAS-60-LaBrigada.md`, I-06.
- Fix menor de concordancia: "1 objetos por corregir" → recurso `plurals` de Android
  (`lugar_riesgos_restantes`), ya no un `%1$d objetos` fijo.
- Creados los tres archivos que la sección 13.1 exige y que ningún plan escribía: `README.md`,
  `database/schema.sql` (exportado de `app/schemas/.../1.json`) y `database/sample_data.sql`
  (transcrito de `SeedData.kt`: 8 lugares, 36 objetos de riesgo, 11 insignias).
- **Firu (la mascota) integrada por primera vez:** el handoff (sección 0.1) decía "ya generada y
  aprobada, solo falta el post-proceso", pero nunca se había hecho — no existía
  `drawable-nodpi/` ni una sola referencia a `firu_` en el código; la mascota solo aparecía como
  texto. Se escribió `documentos-fuente/_scripts-generadores/preparar_firu_labrigada.py` (recorta
  la hoja de contactos en grilla 4x2, quita el fondo por inundación, recorta al bounding box,
  redimensiona a ≤1024 px, exporta WebP calidad 82 — Paso 5 de `02-GUIA-IMAGENES.md`) y se generaron
  las 8 poses (252 KB en total). Se integraron 2 en `LugarScreen`, junto al nombre del lugar:
  `firu_duda` mientras hay objetos por corregir, `firu_saluda` al confirmarse la escena segura —
  justo donde la ficha dice que Firu "aparece al llegar a un lugar y al confirmarlo corregido".
  Verificado jugando: la pose cambia en el momento correcto. Detalle en
  `handoffs/INCIDENCIAS-60-LaBrigada.md`, I-07.
- [PENDIENTE: emulador sigue abierto para seguir verificando; cerrar limpio antes de terminar.
  `git push` pendiente de autorización (sección 15, v8). Fase 2 (Memoria, Manual, capturas, PDF)
  sin empezar.]
