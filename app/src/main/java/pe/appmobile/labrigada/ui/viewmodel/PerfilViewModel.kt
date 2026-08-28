package pe.appmobile.labrigada.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.labrigada.data.repository.BrigadaRepository

data class PerfilUiState(
    val alias: String = "",
    val avatarId: Int = 0,
    val cargando: Boolean = true,
    val guardado: Boolean = false,
)

/**
 * Sirve tanto a la última página del onboarding (primera elección) como a la pantalla de
 * perfil alcanzable desde el Home (edición posterior) -- sección 5.11 del maestro. Si todavía
 * no hay perfil guardado, arranca con [aliasPorDefecto] del mundo de la app, nunca vacío ni con
 * el nombre real del niño.
 */
class PerfilViewModel(
    private val repository: BrigadaRepository,
    private val aliasPorDefecto: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val perfil = repository.obtenerPerfil()
            _uiState.value = PerfilUiState(
                alias = perfil?.alias ?: aliasPorDefecto,
                avatarId = perfil?.avatarId ?: 0,
                cargando = false,
            )
        }
    }

    fun cambiarAlias(nuevo: String) {
        _uiState.value = _uiState.value.copy(alias = nuevo, guardado = false)
    }

    fun elegirAvatar(id: Int) {
        _uiState.value = _uiState.value.copy(avatarId = id, guardado = false)
    }

    fun guardar() {
        val estado = _uiState.value
        val aliasFinal = estado.alias.trim().ifBlank { aliasPorDefecto }
        viewModelScope.launch {
            repository.guardarPerfil(alias = aliasFinal, avatarId = estado.avatarId)
            _uiState.value = _uiState.value.copy(alias = aliasFinal, guardado = true)
        }
    }

    class Factory(
        private val repository: BrigadaRepository,
        private val aliasPorDefecto: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = PerfilViewModel(repository, aliasPorDefecto) as T
    }
}
