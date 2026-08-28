package pe.appmobile.labrigada.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.domain.model.ResultadoSimulacro
import pe.appmobile.labrigada.ui.art.IlustracionObjetoRiesgo
import pe.appmobile.labrigada.ui.components.FichaArrastrable
import pe.appmobile.labrigada.ui.components.ZonaSoltar
import pe.appmobile.labrigada.ui.viewmodel.LugarUiState
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun LugarScreen(
    uiState: LugarUiState,
    onCorregirObjeto: (String) -> Unit,
) {
    if (uiState.cargando) return
    val lugar = uiState.lugar ?: return

    var mostrarAyuda by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(
                        if (uiState.escenaSegura) R.drawable.firu_saluda else R.drawable.firu_duda,
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(lugar.nombre, style = MaterialTheme.typography.headlineLarge)
            }
            val ayudaCd = stringResource(R.string.lugar_cd_ayuda)
            IconButton(
                onClick = { mostrarAyuda = !mostrarAyuda },
                modifier = Modifier.size(56.dp).semantics { contentDescription = ayudaCd },
            ) { Icon(Icons.Filled.Info, contentDescription = null) }
        }
        Text(
            pluralStringResource(R.plurals.lugar_riesgos_restantes, uiState.riesgosRestantes, uiState.riesgosRestantes),
            style = MaterialTheme.typography.bodyLarge,
        )

        if (mostrarAyuda) {
            val primerPendiente = uiState.objetos.firstOrNull { it.id !in uiState.corregidos }
            if (primerPendiente != null) {
                Text(
                    stringResource(R.string.lugar_ayuda_texto, primerPendiente.nombre),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.lugar_zonas_titulo), style = MaterialTheme.typography.titleLarge)
        var zonas by remember { mutableStateOf(mapOf<String, Rect>()) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.objetos.filterNot { uiState.objetoSeTocaNoSeArrastra(it.id) }.forEach { objeto ->
                val corregido = objeto.id in uiState.corregidos
                val zonaCd = stringResource(R.string.lugar_cd_zona, objeto.nombre)
                ZonaSoltar(
                    modifier = Modifier.size(120.dp).semantics { contentDescription = zonaCd },
                    onPosicionConocida = { zonas = zonas + (objeto.id to it) },
                ) {
                    IlustracionObjetoRiesgo(
                        objetoId = objeto.id,
                        modifier = Modifier.fillMaxSize().let { if (corregido) it else it.alpha(0.25f) },
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.lugar_riesgos_titulo), style = MaterialTheme.typography.titleLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.objetos.forEach { objeto ->
                if (objeto.id in uiState.corregidos) return@forEach

                if (uiState.objetoSeTocaNoSeArrastra(objeto.id)) {
                    // Halo propio (borde del Acento de advertencia) para que se vea distinto de
                    // un objeto arrastrable -- sección 0.4.3 del handoff: si se ven igual, el
                    // niño intenta arrastrarlo y parece que la app no responde.
                    val descripcionTocar = stringResource(R.string.lugar_cd_tocar, objeto.nombre)
                    IlustracionObjetoRiesgo(
                        objetoId = objeto.id,
                        modifier = Modifier
                            .size(120.dp)
                            .border(3.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp))
                            .clickable { onCorregirObjeto(objeto.id) }
                            .semantics { contentDescription = descripcionTocar },
                    )
                } else {
                    val zonaDestino = zonas[objeto.id] ?: Rect.Zero
                    val descripcionArrastrar = stringResource(R.string.lugar_cd_arrastrar, objeto.nombre)
                    FichaArrastrable(
                        zonaDestino = zonaDestino,
                        onSoltadaEnZona = { onCorregirObjeto(objeto.id) },
                        modifier = Modifier.size(120.dp).semantics { contentDescription = descripcionArrastrar },
                    ) {
                        IlustracionObjetoRiesgo(objetoId = objeto.id, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }

        if (uiState.escenaSegura) {
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.lugar_escena_segura), color = MaterialTheme.colorScheme.tertiary)
        }

        uiState.resultadoSimulacro?.let { resultado -> ResultadoDeSimulacro(resultado) }
    }
}

@Composable
private fun ResultadoDeSimulacro(resultado: ResultadoSimulacro) {
    Spacer(Modifier.height(16.dp))
    Text(
        text = if (resultado.paso) stringResource(R.string.lugar_simulacro_paso) else stringResource(R.string.lugar_simulacro_fallo),
        color = if (resultado.paso) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
    )
}
