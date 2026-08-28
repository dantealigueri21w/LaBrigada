package pe.appmobile.labrigada.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.data.repository.BrigadaRepository

@Composable
fun ParentalGateScreen(repository: BrigadaRepository) {
    var desbloqueado by remember { mutableStateOf(false) }
    var progresoMs by remember { mutableStateOf(0L) }
    var presionando by remember { mutableStateOf(false) }

    LaunchedEffect(presionando) {
        while (presionando && progresoMs < 3000L) {
            delay(50)
            progresoMs += 50
        }
        if (progresoMs >= 3000L) desbloqueado = true
        if (!presionando) progresoMs = 0L
    }

    if (!desbloqueado) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            presionando = true
                            tryAwaitRelease()
                            presionando = false
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.gate_instruccion))
                LinearProgressIndicator(progress = { (progresoMs / 3000f).coerceIn(0f, 1f) })
            }
        }
    } else {
        var lugaresCorregidos by remember { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            lugaresCorregidos = repository.obtenerLugaresConEstado().count {
                it.estado.name == "COMPLETADO" || it.estado.name == "DOMINADO"
            }
        }
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.gate_titulo))
            Card(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    "${stringResource(R.string.gate_progreso_titulo)}: $lugaresCorregidos / 8",
                    modifier = Modifier.padding(16.dp),
                )
            }
            AjustesSonidoYVibracion()
            // versionName de defaultConfig (app/build.gradle.kts) -- se mantiene en sincronía a
            // mano porque BuildConfig no está habilitado (evita el buildFeature extra por un
            // solo campo de texto).
            Text(stringResource(R.string.gate_version, "1.0.0"))
        }
    }
}

@Composable
private fun AjustesSonidoYVibracion() {
    var sonido by remember { mutableStateOf(true) }
    var vibracion by remember { mutableStateOf(true) }
    Column {
        Row {
            Text(stringResource(R.string.gate_ajustes_sonido))
            Switch(checked = sonido, onCheckedChange = { sonido = it })
        }
        Row {
            Text(stringResource(R.string.gate_ajustes_vibracion))
            Switch(checked = vibracion, onCheckedChange = { vibracion = it })
        }
    }
}
