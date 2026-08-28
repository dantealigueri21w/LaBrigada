package pe.appmobile.labrigada.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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
    error = ErrorClaro,
    onError = Color.White,
)

private val EsquemaOscuro = darkColorScheme(
    primary = NaranjaSeguridadTexto,
    onPrimary = Color.White,
    secondary = AzulUniforme,
    onSecondary = Color.White,
    tertiary = AmarilloAviso,
    onTertiary = AzulMarino,
    background = AzulMarino,
    onBackground = BlancoCalido,
    surface = AzulMarino,
    onSurface = BlancoCalido,
    error = ErrorOscuro,
    onError = AzulMarino,
)

@Composable
fun LaBrigadaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = LaBrigadaTypography,
        content = content,
    )
}
