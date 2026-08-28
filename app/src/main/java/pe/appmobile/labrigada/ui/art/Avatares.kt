package pe.appmobile.labrigada.ui.art

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import pe.appmobile.labrigada.ui.theme.AmarilloAviso
import pe.appmobile.labrigada.ui.theme.AzulMarino
import pe.appmobile.labrigada.ui.theme.AzulUniforme
import pe.appmobile.labrigada.ui.theme.NaranjaSeguridad

/** Los 12 avatares de perfil de la ficha (sección 5.11), a la espera del paso de arte SVG. */
const val CANTIDAD_AVATARES = 12

private val COLORES_AVATAR = listOf(
    NaranjaSeguridad, AzulUniforme, AmarilloAviso, AzulMarino,
)

@Composable
fun Avatar(avatarId: Int, modifier: Modifier = Modifier) {
    val color = COLORES_AVATAR[avatarId % COLORES_AVATAR.size]
    Canvas(modifier = modifier.clip(CircleShape)) {
        drawRect(brush = Brush.radialGradient(listOf(color, color.copy(alpha = 0.6f))))
        drawCircle(color = Color.White.copy(alpha = 0.25f), radius = size.minDimension * 0.18f)
    }
}
