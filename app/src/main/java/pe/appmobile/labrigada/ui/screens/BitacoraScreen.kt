package pe.appmobile.labrigada.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.data.repository.ItemBitacora
import pe.appmobile.labrigada.ui.viewmodel.BitacoraUiState

@Composable
fun BitacoraScreen(uiState: BitacoraUiState, onVolver: () -> Unit) {
    if (uiState.cargando) return

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val volverCd = stringResource(R.string.bitacora_cd_volver)
            IconButton(onClick = onVolver, modifier = Modifier.semantics { contentDescription = volverCd }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(stringResource(R.string.bitacora_titulo), style = MaterialTheme.typography.headlineLarge)
        }

        if (uiState.items.isEmpty()) {
            Text(
                text = stringResource(R.string.bitacora_vacia),
                modifier = Modifier.padding(top = 96.dp, start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 88.dp)) {
                items(uiState.items) { item -> ItemDeBitacora(item) }
            }
        }
    }
}

@Composable
private fun ItemDeBitacora(item: ItemBitacora) {
    val descripcion = stringResource(R.string.bitacora_item_desc, item.nombreLugar)
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp).semantics { contentDescription = descripcion },
    ) {
        Text(descripcion, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
    }
}
