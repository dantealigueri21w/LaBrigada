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
 * perfil alcanzable desde el Home (edición posterior) -- sección 5.11 del maestro.
 * [aliasPorDefecto] es del mundo de la app, nunca el nombre real del niño, y lo pasa quien
 * construye este ViewModel porque un ViewModel plano no tiene Context para leer recursos.
 *
 * Mientras no haya perfil guardado el campo arranca VACÍO, no con ese alias precargado: tenerlo
 * escrito obligaba a borrarlo letra por letra antes de poder poner el propio. El alias por
 * defecto se sigue garantizando al guardar ([guardar] usa `ifBlank`), y la pantalla lo muestra
 * como placeholder.
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
                alias = perfil?.alias.orEmpty(),
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
