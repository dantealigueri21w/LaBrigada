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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.domain.model.ResultadoSimulacro
import pe.appmobile.labrigada.ui.art.IlustracionObjetoRiesgo
import pe.appmobile.labrigada.ui.components.FichaArrastrable
import pe.appmobile.labrigada.ui.components.ZonaSoltar
import pe.appmobile.labrigada.ui.theme.coloresDeApoyo
import pe.appmobile.labrigada.ui.viewmodel.LugarUiState

@Composable
fun LugarScreen(
    uiState: LugarUiState,
    onCorregirObjeto: (String) -> Unit,
    onVolver: () -> Unit = {},
) {
    if (uiState.cargando) return
    val lugar = uiState.lugar ?: return

    var mostrarAyuda by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()

    // Cuando el lugar queda seguro, el panel de cierre nace al final de una pantalla larga y
    // fuera de la vista: el niño terminaba el lugar y no veía ninguna señal de haber terminado.
    LaunchedEffect(uiState.escenaSegura) {
        if (uiState.escenaSegura) scroll.animateScrollTo(scroll.maxValue)
    }

    // Con scroll a propósito: entre las zonas seguras y los objetos hay hasta diez fichas de
    // 120dp, y sin scroll el FlowRow descarta en silencio las que no caben -- quedaban objetos
    // imposibles de corregir y el mensaje final nunca se alcanzaba.
    Column(modifier = Modifier.fillMaxSize().verticalScroll(scroll).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Sin esto solo se salía del lugar con el botón atrás del sistema, que en un
                // teléfono con navegación por gestos no es evidente para un niño.
                val volverCd = stringResource(R.string.lugar_cd_volver)
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier.size(56.dp).semantics { contentDescription = volverCd },
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
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

        // Tarjeta de Firu: qué se hace en este lugar y con qué gesto. Los dos gestos son
        // distintos (arrastrar un objeto, tocar una conducta) y hasta ahora no había una sola
        // línea que lo dijera; solo se descubría probando.
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    stringResource(R.string.lugar_como_se_juega_titulo),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(stringResource(R.string.lugar_como_se_juega_arrastrar), style = MaterialTheme.typography.bodyMedium)
                if (uiState.objetos.any { uiState.objetoSeTocaNoSeArrastra(it.id) }) {
                    Text(stringResource(R.string.lugar_como_se_juega_tocar), style = MaterialTheme.typography.bodyMedium)
                }
                if (uiState.objetos.any { !it.esRiesgo }) {
                    // Aviso del distractor (sección 5.12 del maestro): sin esto, encontrar un
                    // objeto que ya está bien se siente como un bug de la app, no como parte
                    // del reto.
                    Text(stringResource(R.string.lugar_como_se_juega_distractor), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Text(
            pluralStringResource(R.plurals.lugar_riesgos_restantes, uiState.riesgosRestantes, uiState.riesgosRestantes),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            stringResource(R.string.lugar_progreso, uiState.corregidos.size, uiState.objetos.size),
            style = MaterialTheme.typography.bodyMedium,
        )

        // Confirmación en palabras de lo último que se corrigió: mientras quede algo por hacer,
        // esta línea es la única señal de que la acción sí contó.
        if (!uiState.escenaSegura) {
            val ultimo = uiState.objetos.firstOrNull { it.id == uiState.ultimoCorregidoId }
            if (ultimo != null) {
                // Un distractor nunca entra a "corregidos" (sección 5.12): que no esté ahí es
                // la señal de que fue un distractor, no un objeto de riesgo real.
                val mensaje = when {
                    !ultimo.esRiesgo -> R.string.lugar_distractor_ya_estaba_bien
                    // Una conducta no se "guarda en su lugar": se deja de hacer. Confirmar las
                    // dos cosas con la misma frase dejaba mensajes sin sentido ("Cruzar la
                    // pista sin mirar ya está en su lugar seguro").
                    uiState.objetoSeTocaNoSeArrastra(ultimo.id) -> R.string.lugar_conducta_corregida
                    else -> R.string.lugar_objeto_corregido
                }
                Text(
                    stringResource(mensaje, ultimo.nombre),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.coloresDeApoyo.exito,
                )
            }
        }

        if (mostrarAyuda) {
            // Nunca sobre un distractor: una pista tiene que apuntar a algo que de verdad hay
            // que corregir, o "ayuda" empezaría a significar "toca esto al azar".
            val primerPendiente = uiState.objetos.firstOrNull { it.esRiesgo && it.id !in uiState.corregidos }
            if (primerPendiente != null) {
                val textoAyuda = if (uiState.objetoSeTocaNoSeArrastra(primerPendiente.id)) {
                    stringResource(R.string.lugar_ayuda_texto_tocar, primerPendiente.nombre)
                } else {
                    stringResource(R.string.lugar_ayuda_texto, primerPendiente.nombre)
                }
                Text(
                    textoAyuda,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.lugar_zonas_titulo), style = MaterialTheme.typography.titleLarge)
        var zonas by remember { mutableStateOf(mapOf<String, Rect>()) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Un distractor no tiene lugar seguro propio: ya está bien donde está, así que
            // nunca aparece aquí (sección 5.12).
            uiState.objetos.filterNot { uiState.objetoSeTocaNoSeArrastra(it.id) || !it.esRiesgo }.forEach { objeto ->
                val corregido = objeto.id in uiState.corregidos
                val zonaCd = stringResource(R.string.lugar_cd_zona, objeto.nombre)
                Column(
                    modifier = Modifier.width(120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ZonaSoltar(
                        // El hueco vacío lleva contorno propio: la silueta al 25% desaparecía
                        // contra el fondo oscuro y no se veía que ahí HUBIERA un hueco donde
                        // soltar. Cuando ya está corregido el contorno sobra, el objeto se ve.
                        modifier = Modifier
                            .size(120.dp)
                            .let {
                                if (corregido) {
                                    it
                                } else {
                                    it.border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                                }
                            }
                            .semantics { contentDescription = zonaCd },
                        onPosicionConocida = { zonas = zonas + (objeto.id to it) },
                    ) {
                        IlustracionObjetoRiesgo(
                            objetoId = objeto.id,
                            modifier = Modifier.fillMaxSize().let { if (corregido) it else it.alpha(0.25f) },
                        )
                    }
                    // Qué hueco es cuál: la silueta atenuada sola no lo dice, y varios objetos
                    // comparten familia visual (cables, recipientes, muebles).
                    Text(
                        objeto.nombre,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // El bloque entero solo mientras quede algo debajo: con el lugar ya seguro quedaba un
        // "Riesgos por corregir" encabezando el vacío (o, con distractores, encabezando cosas
        // que ya no hay que corregir).
        if (!uiState.escenaSegura) {
        Text(stringResource(R.string.lugar_riesgos_titulo), style = MaterialTheme.typography.titleLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.objetos.forEach { objeto ->
                if (objeto.id in uiState.corregidos) return@forEach

                if (!objeto.esRiesgo) {
                    // Distractor: mismo gesto de toque que una conducta, pero SIN el halo de
                    // advertencia -- ese color significa "esto necesita acción" y un distractor
                    // es exactamente lo contrario (sección 5.12). Tocarlo nunca lo hace
                    // desaparecer de aquí: mirarlo de nuevo no tiene costo.
                    val descripcionDistractor = stringResource(R.string.lugar_cd_distractor, objeto.nombre)
                    Column(
                        modifier = Modifier.width(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        IlustracionObjetoRiesgo(
                            objetoId = objeto.id,
                            modifier = Modifier
                                .size(120.dp)
                                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                                .clickable { onCorregirObjeto(objeto.id) }
                                .semantics { contentDescription = descripcionDistractor },
                        )
                        EtiquetaDeObjeto(objeto = objeto, objetos = uiState.objetos, gesto = null)
                    }
                } else if (uiState.objetoSeTocaNoSeArrastra(objeto.id)) {
                    // Halo propio (borde del Acento de advertencia) para que se vea distinto de
                    // un objeto arrastrable -- sección 0.4.3 del handoff: si se ven igual, el
                    // niño intenta arrastrarlo y parece que la app no responde.
                    val descripcionTocar = stringResource(R.string.lugar_cd_tocar, objeto.nombre)
                    Column(
                        modifier = Modifier.width(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        IlustracionObjetoRiesgo(
                            objetoId = objeto.id,
                            modifier = Modifier
                                .size(120.dp)
                                // NO colorScheme.tertiary directo: ese par da 1.54:1 contra el
                                // fondo claro (sección 6.1 del maestro). advertencia sí está
                                // calculado contra el fondo, en los dos temas.
                                .border(3.dp, MaterialTheme.coloresDeApoyo.advertencia, RoundedCornerShape(16.dp))
                                .clickable { onCorregirObjeto(objeto.id) }
                                .semantics { contentDescription = descripcionTocar },
                        )
                        EtiquetaDeObjeto(objeto = objeto, objetos = uiState.objetos, gesto = R.string.lugar_gesto_tocar)
                    }
                } else {
                    val zonaDestino = zonas[objeto.id] ?: Rect.Zero
                    val descripcionArrastrar = stringResource(R.string.lugar_cd_arrastrar, objeto.nombre)
                    Column(
                        modifier = Modifier.width(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        FichaArrastrable(
                            zonaDestino = zonaDestino,
                            onSoltadaEnZona = { onCorregirObjeto(objeto.id) },
                            modifier = Modifier.size(120.dp).semantics { contentDescription = descripcionArrastrar },
                        ) {
                            IlustracionObjetoRiesgo(objetoId = objeto.id, modifier = Modifier.fillMaxSize())
                        }
                        EtiquetaDeObjeto(objeto = objeto, objetos = uiState.objetos, gesto = R.string.lugar_gesto_arrastrar)
                    }
                }
            }
        }
        }

        if (uiState.escenaSegura) {
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.lugar_escena_segura),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.coloresDeApoyo.exito,
            )
        }

        uiState.resultadoSimulacro?.let { resultado -> ResultadoDeSimulacro(resultado) }

        // Volver al mapa es un acto del niño, no un salto automático: si la pantalla se cerrara
        // sola, el mensaje de cierre no llegaría a leerse.
        if (uiState.escenaSegura) {
            Spacer(Modifier.height(12.dp))
            Button(onClick = onVolver) { Text(stringResource(R.string.lugar_volver_al_mapa)) }
        }
        Spacer(Modifier.height(16.dp))
    }
}

/**
 * Nombre del objeto, el gesto que lo corrige y, cuando la base lo define, la regla de distancia
 * que hay que respetar ("lejos de: la vela"). Ese dato ya existía en `distanciaMinimaDeId` y solo
 * lo usaba el motor: en pantalla, dos objetos que había que separar se veían igual que los demás.
 */
@Composable
private fun EtiquetaDeObjeto(objeto: ObjetoRiesgoEntity, objetos: List<ObjetoRiesgoEntity>, gesto: Int?) {
    Text(objeto.nombre, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    // gesto es null solo para un distractor: no hay ninguna acción que pedirle al niño sobre
    // algo que ya está bien.
    if (gesto != null) {
        Text(
            stringResource(gesto),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
    }
    val debeAlejarseDe = objeto.distanciaMinimaDeId?.let { id -> objetos.firstOrNull { it.id == id }?.nombre }
    if (debeAlejarseDe != null) {
        Text(
            stringResource(R.string.lugar_lejos_de, debeAlejarseDe),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ResultadoDeSimulacro(resultado: ResultadoSimulacro) {
    Spacer(Modifier.height(16.dp))
    Text(
        text = if (resultado.paso) stringResource(R.string.lugar_simulacro_paso) else stringResource(R.string.lugar_simulacro_fallo),
        style = MaterialTheme.typography.titleMedium,
        color = if (resultado.paso) MaterialTheme.coloresDeApoyo.exito else MaterialTheme.colorScheme.error,
    )
}
