package pe.appmobile.labrigada.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta de la ficha (fichas/60-DETECTOR-DE-RIESGOS.md, sección "Paleta")
val NaranjaSeguridad = Color(0xFFE8622C)        // hex oficial -- SOLO ilustraciones/iconos sin texto
val NaranjaSeguridadTexto = Color(0xFFBA4E23)   // variante oscurecida -- unica superficie con texto (5.00:1 con blanco)
val AzulUniforme = Color(0xFF2C5F8A)
val AzulMarino = Color(0xFF1B2E3D)
val AmarilloAviso = Color(0xFFF2C230)
val BlancoCalido = Color(0xFFF7F5F0)

// Error: la ficha no define uno propio. Mismo par fijado para las 5 apps del lote.
val ErrorClaro = Color(0xFFB3261E)
val ErrorOscuro = Color(0xFFF2B8B5)

// Variantes claras de la paleta, SOLO para el esquema oscuro. Los tonos oficiales (naranja de
// texto, azul uniforme) son oscuros a propósito: están calculados para leerse sobre el blanco
// cálido. Sobre el azul marino del modo oscuro quedan casi invisibles (menos de 2:1), así que
// cada uno necesita su gemelo claro -- misma familia de color, contraste invertido.
val NaranjaSeguridadClaro = Color(0xFFFF9B6B)   // 8.9:1 sobre AzulMarino
val AzulUniformeClaro = Color(0xFF9CC9EE)       // 9.6:1 sobre AzulMarino

// Verde de confirmación: no está en la paleta de la ficha, pero hacía falta un color con
// significado de "esto quedó bien" que se lea en los dos modos. AmarilloAviso se usaba para eso
// y sobre el blanco cálido daba 1.6:1 -- el mensaje "el lugar quedó seguro" era invisible en
// modo claro. El amarillo se queda donde sí funciona: como relleno con texto oscuro encima.
val VerdeSeguroTexto = Color(0xFF1B6B3A)        // 5.3:1 sobre BlancoCalido
val VerdeSeguroClaro = Color(0xFF7BDCA0)        // 9.8:1 sobre AzulMarino

// Halo de advertencia (objetos que se corrigen tocando, en vez de con onTertiary encima):
// AmarilloAviso puro es 1.54:1 sobre BlancoCalido -- un borde casi invisible en el modo que
// trae el teléfono por defecto (sección 6.1 del maestro, la app que originó el hallazgo era
// esta misma). Un solo tono no sirve para los dos fondos: oscurecido pasa en claro y cae en
// oscuro, sin oscurecer pasa en oscuro y falla en claro.
val AdvertenciaBordeClaro = Color(0xFF856A1A)   // 4.7:1 sobre BlancoCalido
val AdvertenciaBordeOscuro = AmarilloAviso      // 8.3:1 sobre AzulMarino, ya cumplía

// Tonos de contenedor (tarjetas, campos, bordes). Sin estos, Material rellena los suyos por
// defecto -- los lilas de la paleta base -- y una tarjeta de Firu aparecía en violeta dentro de
// una app naranja y azul. Son la misma familia del fondo de cada modo, un paso de contraste.
val BeigeContenedor = Color(0xFFECE7DE)         // sobre el blanco cálido del modo claro
val AzulContenedor = Color(0xFF25394A)          // sobre el azul marino del modo oscuro
val GrisBordeClaro = Color(0xFF6E7C89)          // 3.9:1 sobre BlancoCalido (borde, no texto)
val GrisBordeOscuro = Color(0xFF9AAAB8)         // 6.9:1 sobre AzulMarino
