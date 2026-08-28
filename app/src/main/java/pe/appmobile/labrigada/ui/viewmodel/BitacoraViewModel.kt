package pe.appmobile.labrigada.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.labrigada.data.repository.BrigadaRepository
import pe.appmobile.labrigada.data.repository.ItemBitacora

data class BitacoraUiState(val items: List<ItemBitacora> = emptyList(), val cargando: Boolean = true)

class BitacoraViewModel(private val repository: BrigadaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(BitacoraUiState())
    val uiState: StateFlow<BitacoraUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = BitacoraUiState(items = repository.obtenerBitacora(), cargando = false)
        }
    }

    class Factory(private val repository: BrigadaRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = BitacoraViewModel(repository) as T
    }
}
