package pe.appmobile.labrigada.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.labrigada.data.repository.BrigadaRepository
import pe.appmobile.labrigada.data.repository.LugarConEstado

data class HomeUiState(
    val lugares: List<LugarConEstado> = emptyList(),
    val aliasPerfil: String? = null,
    val cargando: Boolean = true,
)

class HomeViewModel(private val repository: BrigadaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sembrarSiEsPrimerLanzamiento()
            cargar()
        }
    }

    fun recargar() {
        viewModelScope.launch { cargar() }
    }

    private suspend fun cargar() {
        _uiState.value = HomeUiState(
            lugares = repository.obtenerLugaresConEstado(),
            aliasPerfil = repository.obtenerPerfil()?.alias,
            cargando = false,
        )
    }

    class Factory(private val repository: BrigadaRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
    }
}
