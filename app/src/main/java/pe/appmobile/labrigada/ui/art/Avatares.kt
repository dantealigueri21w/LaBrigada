package pe.appmobile.labrigada.ui.art

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import pe.appmobile.labrigada.R

/** Los 12 avatares de perfil de la ficha (sección 5.11), arte real SVG -> VectorDrawable. */
const val CANTIDAD_AVATARES = 12

private val AVATAR_DRAWABLES = intArrayOf(
    R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4,
    R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8,
    R.drawable.avatar_9, R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12,
)

/**
 * Un nombre por avatar (sección 4.4.1 del maestro), no un número: "avatar 5" no describe nada,
 * y con doce siluetas de verdad distintas cada una sí tiene algo propio que nombrar.
 */
val AVATAR_NOMBRES_RES = intArrayOf(
    R.string.perfil_cd_avatar_1, R.string.perfil_cd_avatar_2, R.string.perfil_cd_avatar_3,
    R.string.perfil_cd_avatar_4, R.string.perfil_cd_avatar_5, R.string.perfil_cd_avatar_6,
    R.string.perfil_cd_avatar_7, R.string.perfil_cd_avatar_8, R.string.perfil_cd_avatar_9,
    R.string.perfil_cd_avatar_10, R.string.perfil_cd_avatar_11, R.string.perfil_cd_avatar_12,
)

@Composable
fun Avatar(avatarId: Int, modifier: Modifier = Modifier) {
    val drawable = AVATAR_DRAWABLES[avatarId.mod(CANTIDAD_AVATARES)]
    Image(painter = painterResource(id = drawable), contentDescription = null, modifier = modifier)
}
