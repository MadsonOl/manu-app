package com.manu.manu_app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.manu.manu_app.data.repository.AuthRepository
import com.manu.manu_app.ui.screens.chamados.ChamadoDetalheScreen
import com.manu.manu_app.ui.screens.chamados.ChamadosScreen
import com.manu.manu_app.ui.screens.empresas.EmpresasScreen
import com.manu.manu_app.ui.screens.home.HomeScreen
import com.manu.manu_app.ui.screens.login.LoginScreen
import com.manu.manu_app.ui.screens.ordens.GerarOsScreen
import com.manu.manu_app.ui.screens.ordens.OrdensScreen
import com.manu.manu_app.ui.screens.profissionais.ProfissionaisScreen
import com.manu.manu_app.ui.screens.solicitante.SolicitanteScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val startDestination = if (AuthRepository().isLoggedIn()) {
        Routes.CHAMADOS
    } else {
        Routes.HOME
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200)) +
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(200)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) +
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200)) +
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(200)
            )
        }
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Routes.SOLICITANTE) {
            SolicitanteScreen(navController = navController)
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.CHAMADOS) {
            GestorScaffold(navController = navController) {
                ChamadosScreen(navController = navController)
            }
        }
        composable(
            Routes.CHAMADO_DETALHE,
            arguments = listOf(navArgument("chamadoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("chamadoId") ?: return@composable
            ChamadoDetalheScreen(navController = navController, chamadoId = id)
        }
        composable(
            Routes.GERAR_OS,
            arguments = listOf(navArgument("chamadoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("chamadoId") ?: return@composable
            GerarOsScreen(navController = navController, chamadoId = id)
        }
        composable(Routes.ORDENS) {
            GestorScaffold(navController = navController) {
                OrdensScreen(navController = navController)
            }
        }
        composable(Routes.PROFISSIONAIS) {
            GestorScaffold(navController = navController) {
                ProfissionaisScreen(navController = navController)
            }
        }
        composable(Routes.EMPRESAS) {
            GestorScaffold(navController = navController) {
                EmpresasScreen(navController = navController)
            }
        }
    }
}
