package pe.appmobile.labrigada.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.ui.art.Avatar
import pe.appmobile.labrigada.ui.art.CANTIDAD_AVATARES
import pe.appmobile.labrigada.ui.theme.AmarilloAviso
import pe.appmobile.labrigada.ui.theme.AzulMarino

/**
 * Cuadrícula armada a mano (`chunked(4)` en `Row`, nunca `LazyVerticalGrid` -- sección 7.1
 * punto 6 del maestro: 12 avatares es una cantidad chica y fija) para elegir uno de los 12
 * avatares de la ficha. Cada fila hace scroll horizontal propio para que los 4 objetivos de
 * 120dp sigan siendo full-size en un teléfono angosto, en vez de encogerse por debajo del
 * mínimo de accesibilidad de la sección 6.
 */
@Composable
fun SelectorDeAvatares(
    seleccionado: Int,
    onSeleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        (0 until CANTIDAD_AVATARES).chunked(4).forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                fila.forEach { avatarId ->
                    ItemAvatarSeleccionable(
                        avatarId = avatarId,
                        seleccionado = avatarId == seleccionado,
                        onClick = { onSeleccionar(avatarId) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ItemAvatarSeleccionable(avatarId: Int, seleccionado: Boolean, onClick: () -> Unit) {
    val nombreBase = stringResource(R.string.perfil_cd_avatar, avatarId + 1)
    val descripcionCompleta = if (seleccionado) {
        stringResource(R.string.perfil_cd_avatar_elegida, avatarId + 1)
    } else {
        nombreBase
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .then(
                // El borde sale del esquema, NO de AzulMarino fijo: en modo oscuro el fondo ES
                // ese azul marino y la insignia elegida no se distinguía de las demás.
                if (seleccionado) {
                    Modifier.border(width = 3.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                } else {
                    Modifier
                },
            )
            .clickable(onClickLabel = nombreBase, role = Role.Button, onClick = onClick)
            .semantics { contentDescription = descripcionCompleta }
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Avatar(avatarId = avatarId, modifier = Modifier.size(90.dp))

        if (seleccionado) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(26.dp)
                    .background(AmarilloAviso, CircleShape)
                    .border(width = 1.5.dp, color = AzulMarino, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "✓", color = AzulMarino, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
