package pe.appmobile.labrigada.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.ui.components.SelectorDeAvatares

private data class PantallaOnboarding(val tituloRes: Int, val textoRes: Int)

private val PANTALLAS = listOf(
    PantallaOnboarding(R.string.onboarding_1_titulo, R.string.onboarding_1_texto),
    PantallaOnboarding(R.string.onboarding_2_titulo, R.string.onboarding_2_texto),
    PantallaOnboarding(R.string.onboarding_3_titulo, R.string.onboarding_3_texto),
    PantallaOnboarding(R.string.onboarding_4_titulo, R.string.onboarding_4_texto),
)

/**
 * Termina eligiendo alias y avatar (sección 5.11 del maestro), no solo con pantallas
 * informativas: quien construye esta app no vuelve a repetir el hueco de Base de Campo y
 * Huellario, donde el perfil quedó declarado en Room pero nunca se llenó desde ningún lado.
 */
@Composable
fun OnboardingScreen(aliasPorDefecto: String, onTerminar: (alias: String, avatarId: Int) -> Unit) {
    var indice by remember { mutableIntStateOf(0) }
    val esPaginaDePerfil = indice == PANTALLAS.size

    if (!esPaginaDePerfil) {
        val pantalla = PANTALLAS[indice]
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(pantalla.tituloRes), style = MaterialTheme.typography.headlineLarge)
            Text(stringResource(pantalla.textoRes), style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { indice++ }) { Text(stringResource(R.string.onboarding_continuar)) }
        }
    } else {
        var alias by remember { mutableStateOf(aliasPorDefecto) }
        var avatarId by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(R.string.onboarding_perfil_titulo), style = MaterialTheme.typography.headlineLarge)
            Text(stringResource(R.string.onboarding_perfil_texto), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                singleLine = true,
                label = { Text(stringResource(R.string.perfil_alias_label)) },
                placeholder = { Text(stringResource(R.string.perfil_alias_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.perfil_avatar_titulo), style = MaterialTheme.typography.titleLarge)
            SelectorDeAvatares(seleccionado = avatarId, onSeleccionar = { avatarId = it }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Button(onClick = { onTerminar(alias.trim().ifBlank { aliasPorDefecto }, avatarId) }) {
                Text(stringResource(R.string.onboarding_empezar))
            }
        }
    }
}
