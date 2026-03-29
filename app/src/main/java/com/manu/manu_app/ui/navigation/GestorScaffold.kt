package com.manu.manu_app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.manu.manu_app.ui.theme.Surface

@Composable
fun GestorScaffold(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val items = listOf(
        BottomNavItem(label = "Chamados", route = Routes.CHAMADOS, icon = Icons.Filled.List),
        BottomNavItem(label = "Ordens", route = Routes.ORDENS, icon = Icons.Filled.Assignment),
        BottomNavItem(label = "Empresas", route = Routes.EMPRESAS, icon = Icons.Filled.Business),
        BottomNavItem(label = "Profissionais", route = Routes.PROFISSIONAIS, icon = Icons.Filled.Person)
    )

    val currentRoute by navController.currentBackStackEntryAsState()
    val currentDestination = currentRoute?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Surface
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(Routes.CHAMADOS) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) {
        content()
    }
}
