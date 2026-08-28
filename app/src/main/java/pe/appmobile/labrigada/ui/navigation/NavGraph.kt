package pe.appmobile.labrigada.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.appmobile.labrigada.R
import pe.appmobile.labrigada.data.repository.BrigadaRepository
import pe.appmobile.labrigada.ui.screens.BitacoraScreen
import pe.appmobile.labrigada.ui.screens.HomeScreen
import pe.appmobile.labrigada.ui.screens.LugarScreen
import pe.appmobile.labrigada.ui.screens.OnboardingScreen
import pe.appmobile.labrigada.ui.screens.ParentalGateScreen
import pe.appmobile.labrigada.ui.screens.PerfilScreen
import pe.appmobile.labrigada.ui.viewmodel.BitacoraViewModel
import pe.appmobile.labrigada.ui.viewmodel.HomeViewModel
import pe.appmobile.labrigada.ui.viewmodel.LugarViewModel
import pe.appmobile.labrigada.ui.viewmodel.PerfilViewModel

object Rutas {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val LUGAR = "lugar/{lugarId}"
    const val PARENTAL_GATE = "parental_gate"
    const val PERFIL = "perfil"
    const val BITACORA = "bitacora"
    fun lugar(id: String) = "lugar/$id"
}

@Composable
fun NavGraph(repository: BrigadaRepository, esPrimerLanzamiento: Boolean) {
    val navController: NavHostController = rememberNavController()
    val aliasPorDefecto = stringResource(R.string.perfil_alias_placeholder)

    NavHost(navController = navController, startDestination = if (esPrimerLanzamiento) Rutas.ONBOARDING else Rutas.HOME) {
        composable(Rutas.ONBOARDING) {
            val viewModel: PerfilViewModel = viewModel(factory = PerfilViewModel.Factory(repository, aliasPorDefecto))
            OnboardingScreen(
                aliasPorDefecto = aliasPorDefecto,
                onTerminar = { alias, avatarId ->
                    // Se persiste antes de dejar el onboarding para que el Home ya encuentre un
                    // perfil real la primera vez que se compone (seccion 5.11 del maestro).
                    viewModel.cambiarAlias(alias)
                    viewModel.elegirAvatar(avatarId)
                    viewModel.guardar()
                    navController.navigate(Rutas.HOME) { popUpTo(Rutas.ONBOARDING) { inclusive = true } }
                },
            )
        }
        composable(Rutas.HOME) {
            val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(repository))
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            // Sin esto el Home (raiz del back stack) no se refresca al volver de un lugar recien
            // corregido -- seccion 7.1 punto 5 y 2.2 del archivo de lecciones.
            LifecycleResumeEffect(Unit) {
                viewModel.recargar()
                onPauseOrDispose {}
            }
            HomeScreen(
                uiState = uiState,
                onLugarClick = { navController.navigate(Rutas.lugar(it)) },
                onBitacoraClick = { navController.navigate(Rutas.BITACORA) },
                onPerfilClick = { navController.navigate(Rutas.PERFIL) },
                onParentalGateClick = { navController.navigate(Rutas.PARENTAL_GATE) },
            )
        }
        composable(
            Rutas.LUGAR,
            arguments = listOf(navArgument("lugarId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val lugarId = backStackEntry.arguments?.getString("lugarId") ?: return@composable
            val viewModel: LugarViewModel = viewModel(factory = LugarViewModel.Factory(repository, lugarId))
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LugarScreen(uiState = uiState, onCorregirObjeto = viewModel::corregirObjeto)
        }
        composable(Rutas.PERFIL) {
            val viewModel: PerfilViewModel = viewModel(factory = PerfilViewModel.Factory(repository, aliasPorDefecto))
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            PerfilScreen(
                uiState = uiState,
                onAliasChange = viewModel::cambiarAlias,
                onAvatarChange = viewModel::elegirAvatar,
                onGuardar = viewModel::guardar,
                onVolver = { navController.popBackStack() },
            )
        }
        composable(Rutas.BITACORA) {
            val viewModel: BitacoraViewModel = viewModel(factory = BitacoraViewModel.Factory(repository))
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            BitacoraScreen(uiState = uiState, onVolver = { navController.popBackStack() })
        }
        composable(Rutas.PARENTAL_GATE) {
            ParentalGateScreen(repository = repository)
        }
    }
}
