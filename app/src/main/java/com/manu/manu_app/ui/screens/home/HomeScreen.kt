package com.manu.manu_app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.manu.manu_app.ui.navigation.Routes
import com.manu.manu_app.ui.theme.Background
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var visibleTop by remember { mutableStateOf(false) }
    var visibleBottom by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visibleTop = true
        delay(150)
        visibleBottom = true
    }

    Scaffold(
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visibleTop,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { -60 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = "manu",
                        modifier = Modifier.size(48.dp),
                        tint = Primary
                    )

                    Text(
                        text = "manu",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )

                    Text(
                        text = "Sistema de gestao de manutencoes",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 48.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = visibleBottom,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { 60 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { navController.navigate(Routes.SOLICITANTE) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Abrir Chamado")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { navController.navigate(Routes.LOGIN) },
                        modifier = Modifier.fillMaxWidth(),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            brush = SolidColor(Primary)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Primary
                        )
                    ) {
                        Text("Acesso Gestor")
                    }
                }
            }
        }
    }
}
