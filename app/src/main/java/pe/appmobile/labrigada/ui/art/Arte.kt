package pe.appmobile.labrigada.ui.art

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import pe.appmobile.labrigada.ui.theme.AzulUniforme
import pe.appmobile.labrigada.ui.theme.NaranjaSeguridad

/**
 * Reparto temporal de arte, a la espera del paso de SVG -> VectorDrawable (sección 4.0/4.1.5 del
 * maestro). Estas funciones son el único punto de la app que conoce cada id de objeto/lugar; el
 * paso de arte reemplaza SOLO su interior (por un `Image(painterResource(...))` sobre los
 * vectores reales) sin tocar ninguna pantalla que ya las use. Anotado en BUILD_REPORT.md.
 */
@Composable
fun IlustracionObjetoRiesgo(objetoId: String, modifier: Modifier = Modifier) {
    PiezaEsquematica(id = objetoId, modifier = modifier)
}

@Composable
fun IconoLugar(lugarId: String, modifier: Modifier = Modifier) {
    PiezaEsquematica(id = lugarId, modifier = modifier)
}

@Composable
private fun PiezaEsquematica(id: String, modifier: Modifier) {
    val color = colorDesdeId(id)
    Canvas(modifier = modifier) {
        drawRoundRect(
            brush = Brush.linearGradient(listOf(color, color.copy(alpha = 0.7f))),
            cornerRadius = CornerRadius(size.minDimension * 0.18f),
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(size.width * 0.3f, size.height * 0.25f),
                radius = size.width * 0.4f,
            ),
            radius = size.width * 0.4f,
            center = Offset(size.width * 0.3f, size.height * 0.25f),
        )
    }
}

private fun colorDesdeId(id: String): Color {
    val paleta = listOf(NaranjaSeguridad, AzulUniforme)
    return paleta[(id.hashCode().mod(paleta.size))]
}
