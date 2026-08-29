package pe.appmobile.labrigada.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = NaranjaSeguridadTexto,
    onPrimary = Color.White,
    secondary = AzulUniforme,
    onSecondary = Color.White,
    tertiary = AmarilloAviso,
    onTertiary = AzulMarino,
    background = BlancoCalido,
    onBackground = AzulMarino,
    surface = BlancoCalido,
    onSurface = AzulMarino,
    surfaceVariant = BeigeContenedor,
    onSurfaceVariant = AzulMarino,
    surfaceContainerLowest = BlancoCalido,
    surfaceContainerLow = BeigeContenedor,
    surfaceContainer = BeigeContenedor,
    surfaceContainerHigh = BeigeContenedor,
    surfaceContainerHighest = BeigeContenedor,
    outline = GrisBordeClaro,
    error = ErrorClaro,
    onError = Color.White,
)

// primary y secondary NO son los mismos que en claro: los tonos oficiales están oscurecidos para
// leerse sobre blanco y sobre el azul marino del modo oscuro caen por debajo de 2:1 (un botón
// naranja oscuro sobre fondo azul oscuro, o la pista de Firu en azul sobre azul, desaparecen).
// Se usan los gemelos claros, y en consecuencia el texto ENCIMA de ellos pasa a ser el azul
// marino, no blanco -- si no, el par se invierte y vuelve a ser ilegible.
private val EsquemaOscuro = darkColorScheme(
    primary = NaranjaSeguridadClaro,
    onPrimary = AzulMarino,
    secondary = AzulUniformeClaro,
    onSecondary = AzulMarino,
    tertiary = AmarilloAviso,
    onTertiary = AzulMarino,
    background = AzulMarino,
    onBackground = BlancoCalido,
    surface = AzulMarino,
    onSurface = BlancoCalido,
    surfaceVariant = AzulContenedor,
    onSurfaceVariant = BlancoCalido,
    surfaceContainerLowest = AzulMarino,
    surfaceContainerLow = AzulContenedor,
    surfaceContainer = AzulContenedor,
    surfaceContainerHigh = AzulContenedor,
    surfaceContainerHighest = AzulContenedor,
    outline = GrisBordeOscuro,
    error = ErrorOscuro,
    onError = AzulMarino,
)

/**
 * Colores con significado propio que el esquema de Material no cubre. Vive en un
 * `CompositionLocal` y no en un `if (isSystemInDarkTheme())` suelto a propósito: así sigue al
 * mismo `darkTheme` que recibe [LaBrigadaTheme], incluso cuando se le fuerza el valor (pruebas,
 * vistas previas), en vez de consultar el ajuste del sistema por su cuenta.
 */
@Immutable
data class ColoresDeApoyo(
    /** "Esto quedó bien": confirmaciones, escena segura, perfil guardado. */
    val exito: Color,
    /** Halo de "esto se toca, no se arrastra". Nunca `colorScheme.tertiary` directo contra el
     * fondo: ese par da 1.54:1 en claro (sección 6.1 del maestro). */
    val advertencia: Color,
)

private val ApoyoClaro = ColoresDeApoyo(exito = VerdeSeguroTexto, advertencia = AdvertenciaBordeClaro)
private val ApoyoOscuro = ColoresDeApoyo(exito = VerdeSeguroClaro, advertencia = AdvertenciaBordeOscuro)

private val LocalColoresDeApoyo = staticCompositionLocalOf { ApoyoClaro }

val MaterialTheme.coloresDeApoyo: ColoresDeApoyo
    @Composable @ReadOnlyComposable get() = LocalColoresDeApoyo.current

@Composable
fun LaBrigadaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalColoresDeApoyo provides if (darkTheme) ApoyoOscuro else ApoyoClaro) {
        MaterialTheme(
            colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
            typography = LaBrigadaTypography,
            content = content,
        )
    }
}
