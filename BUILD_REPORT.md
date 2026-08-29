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
  Fase 2 (Memoria, Manual, capturas, PDF) sin empezar.]

## Cierre de Fase 1: push, Actions y verificación del APK entregable

- Rama renombrada `master` → `main`, remoto `origin` configurado a
  `https://github.com/dantealigueri21w/LaBrigada.git`. `git push -u origin main` autorizado y
  hecho (9 commits).
- Workflow `android-build` corrida #1 (commit `ea5825c`): `status: completed`,
  `conclusion: success`. Artifact `apk` confirmado real vía API antes de confiar en el check verde
  (sección 12.1): 13 020 956 bytes, no vacío, no oculto.
- Descarga del artifact por API bloqueada por falta de token en esta sesión (no hay forma de
  correr `gh auth login` sin flujo interactivo); se descargó manualmente desde la página de
  Actions y se copió a `60.LaBrigada/4.LaBrigada.v1.0.0.apk`.
- **Verificación sobre el APK que de verdad se entrega** (sección 14.4), no sobre la build local:
  - `apksigner verify --print-certs`: firmado, `CN=Android Debug` (llave de depuración por
    defecto, sin datos personales).
  - `aapt2 dump badging`: `versionName='1.0.0'` (coincide con el nombre del archivo), sin permiso
    `INTERNET` (el único permiso presente,
    `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`, lo agrega AGP automáticamente y no es de red),
    `icon='res/mipmap-anydpi-v26/ic_launcher.xml'` real.
  - SHA-256 del archivo entregado: `87dc494c3492e82d099af34f01661f8b4abe0aeb230590cd14e35288d35dd86b`.
    No se compara con el hash local (nunca coinciden entre máquinas, sección 15 del maestro).
- Carpeta `60.LaBrigada/` creada con `1.CodigoFuenteLaBrigada.zip` (`git archive`, revisado por
  dentro: sin `.git/`, sin nombres de herramienta, raíz correcta) y `4.LaBrigada.v1.0.0.apk`.
  Faltan los PDF de la Fase 2.

## Pulido de UX tras jugar el APK real (29/08/2026)

Rodrigo volvió a reportar el síntoma de siempre — "los textos aparecían en blanco y no se veía lo
que se hacía" al elegir su nombre — y pidió traer a esta app el mismo tratamiento que ya se le
había dado a La Alforja. Detalle completo en `handoffs/INCIDENCIAS-60-LaBrigada.md`, I-08 a I-11.

**Legibilidad (el bug reportado).** El `Surface` de `MainActivity` de la sesión anterior arreglaba
el fondo de la actividad, pero media paleta seguía calculada solo contra el blanco:

- El degradado del Home tenía `BlancoCalido` y `Color.White` cableados: en modo oscuro el texto
  crema caía sobre fondo crema. Ahora los dos tonos salen de `colorScheme.background`.
- `primary` y `secondary` son tonos oscurecidos *para leerse sobre blanco*; sobre el azul marino
  del esquema oscuro daban menos de 2:1 (la pista de Firu era azul sobre azul). El esquema oscuro
  usa ahora `NaranjaSeguridadClaro` y `AzulUniformeClaro`, con su `on*` invertido a `AzulMarino`.
- El amarillo de aviso se usaba como color de TEXTO para "¡Guardado!" y "el lugar quedó seguro":
  1.6:1 sobre el blanco cálido, o sea invisible en el modo claro (el que trae el teléfono por
  defecto). Esos mensajes pasan a un verde de confirmación con variante por modo.
- El borde de "insignia elegida" era `AzulMarino` fijo, el mismo color del fondo oscuro.
- Se agregan tonos de contenedor propios (sin ellos las tarjetas salían en el lila por defecto de
  Material) y `values-night/themes.xml`, que quita el destello blanco del arranque.
- Todo esto queda **verificado por prueba, no de ojo**: `ContrasteDelTemaTest` calcula el
  contraste WCAG de los siete pares de color que la app usa junta y falla por debajo de 4.5:1, en
  los dos modos.

**Lógica y claridad del juego.**

- Campo de nombre vacío con el alias por defecto de `placeholder`: venía precargado y había que
  borrarlo letra por letra. La garantía de que nunca queda vacío ya vivía en `guardar()`.
- Scroll real en el Home y en la pantalla de lugar: el `FlowRow` descartaba en silencio los
  lugares y objetos que no cabían, y el mensaje de cierre del lugar nacía siempre fuera de vista
  (ahora la pantalla baja sola hasta él al quedar seguro).
- El arrastre pasa a `detectDragGesturesAfterLongPress`: el gesto omnidireccional se tragaba
  cualquier deslizamiento que empezara sobre una ficha y la página no respondía al scroll.
- Se muestran datos que ya existían y solo leía el motor o el lector de pantalla: el nombre de
  cada lugar en el mapa, el nombre de cada objeto y de cada hueco, el gesto que corrige cada uno,
  la regla "tiene que quedar lejos de: …", y cuánto falta para abrir un lugar bloqueado.
- Corregir algo ahora se confirma en palabras, con frase propia para las conductas (una conducta
  se deja de hacer, no se guarda en su lugar), más el progreso "llevas N de M" y una tarjeta de
  Firu que explica el lugar y sus dos gestos.
- El hueco vacío lleva contorno: la silueta al 25% desaparecía contra el fondo oscuro.
- Botón de volver propio en la pantalla de lugar y en la zona de quien acompaña: hasta ahora solo
  se salía con el botón atrás del sistema.

**Verificación:** 90 pruebas unitarias en verde, y el ciclo completo jugado en el emulador en los
dos modos — onboarding, elegir nombre, mapa, Mi Cuarto y La Cocina corregidos por arrastre, La
Calle con sus conductas por toque, y el perfil guardado.

## Auditoría contra el prompt maestro v14: avatares, distractores y un borde ilegible (29/08/2026)

El maestro se actualizó con hallazgos de auditar el código ya entregado de las cinco últimas
apps, y dos de esos hallazgos nombran a La Brigada por su propio código como el caso que originó
la regla. Se aplican los tres a la vez.

**Avatares (sección 4.4/4.4.1): 12 avatares eran 4 dibujos recoloreados.** Comparando los 12
`avatar_N.xml` ignorando el color, solo había 4 siluetas distintas, cada una repetida 3 veces
(`avatar_1` y `avatar_5` eran el mismo dibujo). Además la cabeza se rellenaba con el degradado de
acento (termina en `#1B2E3D`, casi negro) y los ojos eran anillos sin relleno del mismo color que
la cabeza -- invisibles por construcción. Se reemplazan los 12 XML por la salida ya generada y
verificada de `documentos-fuente/_scripts-generadores/gen_avatares_vector.py` (piel con degradado
propio, ojos con esclerótica y pupila, boca bajo la nariz, tocado con color independiente de la
ropa): **12 de 12 siluetas únicas**, verificado con el comando de la 4.4 (`sed` + `md5sum` sobre
los 12 archivos ignorando color). El `contentDescription` de cada avatar pasa de "Insignia N" a un
nombre real ("Brigadista con casco", "Brigadista con rizos y gafas", ...), leído de un arreglo
`AVATAR_NOMBRES_RES` en vez de una plantilla numerada.

**Distractores (sección 5.12): 36 objetos sembrados, 36 eran riesgo, 0 eran seguros.** El maestro
cita el código real de `ObjetoRiesgoEntity` (sin ningún campo que diga "esto no es un riesgo") como
el caso que escribió esta regla: `corregirObjeto()` no podía fallar, así que la mecánica completa
se reducía a "toca todo, en cualquier orden". Se agrega `esRiesgo: Boolean` a la entidad (sin valor
por defecto, para que declararlo sea obligatorio) y 16 distractores nuevos -- 2 por lugar, cada uno
la contraparte YA seguro de un riesgo real de ese mismo lugar (`olla_mango_adentro` junto a
`olla_mango_afuera`, `ventana_con_seguro_puesto` junto a `ventana_sin_seguro`, y así en los 8
lugares) -- dejando la proporción en 16 de 52 (≈30 %, "ronda un tercio" como pide la regla).
`MotorEscena`, `MotorRiesgoRestante` y `MotorSimulacro` ahora ignoran los objetos con
`esRiesgo = false` para decidir si la escena está segura, cuántos riesgos faltan y qué contó como
fallo; un distractor tocado nunca compensa un riesgo real sin corregir (cubierto con tests nuevos
en los tres motores). En pantalla, el distractor vive mezclado entre los riesgos reales (mismo
gesto de toque, sin el halo de advertencia que sí llevan las conductas) y tocarlo deja el mensaje
de Firu "X ya estaba bien. No hacía falta corregirlo." -- nunca "ya está en su lugar seguro", que
no tendría sentido para algo que no se movió.

**Contraste del halo (sección 6.1): el borde del objeto de conducta era 1,54:1.** El maestro mide
el par que la interfaz *realmente* dibuja, no solo los seis del `ColorScheme`: `border(3.dp,
colorScheme.tertiary)` pone el acento directo contra el fondo, no contra `onTertiary`. AmarilloAviso
sobre BlancoCalido da 1,54:1 -- un borde que en el modo claro (el que trae el teléfono por defecto)
casi no se ve. Un solo tono no alcanza para los dos modos: oscurecido pasa en claro y cae bajo 3:1
en oscuro, sin oscurecer pasa en oscuro y falla en claro. Se agrega `advertencia` a
`ColoresDeApoyo` (`AdvertenciaBordeClaro` #856A1A, 4.7:1 sobre BlancoCalido; `AdvertenciaBordeOscuro`
= AmarilloAviso, ya cumplía) y `LugarScreen` usa ese color en vez de `colorScheme.tertiary` directo.
`ContrasteDelTemaTest` suma los tres pares acento-sobre-fondo que pide la sección 6.1
(`primary/background`, `secondary/background`, `advertencia/background`) a los ocho que ya tenía.

**Verificación:** 96 pruebas unitarias en verde (10 nuevas: 2 de contraste, 4 de motores de
dominio para distractores, 2 de pantalla para distractores, más las de conteo de semilla
actualizadas a 7 objetos por lugar en Mi Cuarto), `lintDebug` limpio, `assembleDebug` compila.
`database/schema.sql` y `database/sample_data.sql` actualizados con la columna `esRiesgo` y los 16
distractores (antes documentaban solo los 36 de riesgo).
