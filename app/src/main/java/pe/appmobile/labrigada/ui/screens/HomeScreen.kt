package pe.appmobile.labrigada.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.data.repository.LugarConEstado
import pe.appmobile.labrigada.domain.model.EstadoLugar
import pe.appmobile.labrigada.ui.art.IconoLugar
import pe.appmobile.labrigada.ui.theme.AzulUniforme
import pe.appmobile.labrigada.ui.theme.BlancoCalido
import pe.appmobile.labrigada.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onLugarClick: (String) -> Unit,
    onBitacoraClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onParentalGateClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        FondoCuartel(modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Gesto de la sección 17 del maestro: mantener presionado el logotipo 3 segundos
            // abre la zona de quien acompaña. Nunca un botón visible ni un candado que invite a
            // tocarlo -- un niño de 8 a 12 años no lo hace por accidente.
            Text(
                text = stringResource(R.string.home_titulo),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onParentalGateClick() })
                },
            )
            uiState.aliasPerfil?.let {
                Text(
                    text = stringResource(R.string.home_saludo, it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            if (!uiState.cargando) {
                val corregidos = uiState.lugares.count {
                    it.estado == EstadoLugar.COMPLETADO || it.estado == EstadoLugar.DOMINADO
                }
                Text(
                    text = stringResource(R.string.home_progreso, corregidos, uiState.lugares.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Spacer(Modifier.height(24.dp))

            if (!uiState.cargando) {
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    uiState.lugares.forEach { lugarConEstado ->
                        PuestoDeBrigada(
                            lugarConEstado = lugarConEstado,
                            onClick = { onLugarClick(lugarConEstado.lugar.id) },
                        )
                    }
                }
            } else {
                Spacer(Modifier.weight(1f))
            }

            val perfilCdTexto = stringResource(R.string.home_cd_perfil)
            val bitacoraCdTexto = stringResource(R.string.home_cd_bitacora)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(
                    onClick = onPerfilClick,
                    modifier = Modifier.size(56.dp).semantics { contentDescription = perfilCdTexto },
                ) { Icon(Icons.Filled.Person, contentDescription = null) }
                IconButton(
                    onClick = onBitacoraClick,
                    modifier = Modifier.size(56.dp).semantics { contentDescription = bitacoraCdTexto },
                ) { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }
            }
        }
    }
}

/** Fondo propio del Home -- luz de día uniforme, nunca el tono nocturno de Base de Campo. */
@Composable
private fun FondoCuartel(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawRect(brush = Brush.verticalGradient(listOf(AzulUniforme.copy(alpha = 0.15f), BlancoCalido)))
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.5f), Color.Transparent),
                center = Offset(size.width * 0.5f, size.height * 0.05f),
                radius = size.width * 0.5f,
            ),
            radius = size.width * 0.5f,
            center = Offset(size.width * 0.5f, size.height * 0.05f),
        )
    }
}

private fun textoDeEstado(estado: EstadoLugar): Int = when (estado) {
    EstadoLugar.BLOQUEADO -> R.string.home_estado_bloqueado
    EstadoLugar.DISPONIBLE -> R.string.home_estado_disponible
    EstadoLugar.INICIADO -> R.string.home_estado_iniciado
    EstadoLugar.COMPLETADO -> R.string.home_estado_completado
    EstadoLugar.DOMINADO -> R.string.home_estado_dominado
}

private fun iconoDeEstado(estado: EstadoLugar): ImageVector = when (estado) {
    EstadoLugar.BLOQUEADO -> Icons.Filled.Lock
    EstadoLugar.DISPONIBLE -> Icons.Filled.PlayArrow
    EstadoLugar.INICIADO -> Icons.Filled.Refresh
    EstadoLugar.COMPLETADO -> Icons.Filled.CheckCircle
    EstadoLugar.DOMINADO -> Icons.Filled.Star
}

@Composable
private fun PuestoDeBrigada(lugarConEstado: LugarConEstado, onClick: () -> Unit) {
    val bloqueado = lugarConEstado.estado == EstadoLugar.BLOQUEADO
    val estadoTexto = stringResource(textoDeEstado(lugarConEstado.estado))
    val descripcion = if (bloqueado) {
        "${lugarConEstado.lugar.nombre}, $estadoTexto. " +
            stringResource(R.string.home_falta_para_abrir, lugarConEstado.faltanParaAbrir)
    } else {
        "${lugarConEstado.lugar.nombre}, $estadoTexto"
    }

    Column(
        modifier = Modifier
            .size(120.dp)
            .clickable(enabled = !bloqueado, onClick = onClick)
            .semantics { contentDescription = descripcion },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            IconoLugar(
                lugarId = lugarConEstado.lugar.id,
                modifier = Modifier.fillMaxSize().alpha(if (bloqueado) 0.35f else 1f),
            )
            Icon(
                imageVector = iconoDeEstado(lugarConEstado.estado),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(28.dp).align(Alignment.TopEnd),
            )
        }
        Text(estadoTexto, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}
