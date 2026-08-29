package pe.appmobile.labrigada.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.ui.components.SelectorDeAvatares
import pe.appmobile.labrigada.ui.theme.coloresDeApoyo
import pe.appmobile.labrigada.ui.viewmodel.PerfilUiState

@Composable
fun PerfilScreen(
    uiState: PerfilUiState,
    onAliasChange: (String) -> Unit,
    onAvatarChange: (Int) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit,
) {
    if (uiState.cargando) return

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val volverCd = stringResource(R.string.perfil_cd_volver)
            IconButton(onClick = onVolver, modifier = Modifier.semantics { contentDescription = volverCd }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(stringResource(R.string.perfil_titulo), style = MaterialTheme.typography.headlineLarge)
        }
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = uiState.alias,
            onValueChange = onAliasChange,
            singleLine = true,
            label = { Text(stringResource(R.string.perfil_alias_label)) },
            placeholder = { Text(stringResource(R.string.perfil_alias_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(24.dp))
        Text(stringResource(R.string.perfil_avatar_titulo), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        SelectorDeAvatares(seleccionado = uiState.avatarId, onSeleccionar = onAvatarChange, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))
        Button(onClick = onGuardar) { Text(stringResource(R.string.perfil_guardar)) }

        if (uiState.guardado) {
            Spacer(Modifier.height(8.dp))
            // Verde de confirmación, no el amarillo de aviso: sobre el blanco cálido del modo
            // claro ese amarillo daba 1.6:1 y el "¡Guardado!" no se leía.
            Text(stringResource(R.string.perfil_guardado), color = MaterialTheme.coloresDeApoyo.exito)
        }
    }
}
