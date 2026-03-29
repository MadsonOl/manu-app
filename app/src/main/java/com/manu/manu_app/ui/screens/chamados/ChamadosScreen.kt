package com.manu.manu_app.ui.screens.chamados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.ui.navigation.Routes
import com.manu.manu_app.ui.theme.Background
import com.manu.manu_app.ui.theme.Error
import com.manu.manu_app.ui.theme.ErrorContainer
import com.manu.manu_app.ui.theme.OnBackground
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import com.manu.manu_app.ui.theme.Secondary
import com.manu.manu_app.ui.theme.SecondaryContainer
import com.manu.manu_app.ui.theme.SurfaceVariant
import com.manu.manu_app.ui.theme.Tertiary
import com.manu.manu_app.ui.theme.TertiaryContainer
import com.manu.manu_app.viewmodel.ChamadosEvent
import com.manu.manu_app.viewmodel.ChamadosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChamadosScreen(
    navController: NavController,
    viewModel: ChamadosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var mostrarDialogLogout by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ChamadosEvent.Logout -> {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            }
        }
    }

    if (mostrarDialogLogout) {
        AlertDialog(
            onDismissRequest = { mostrarDialogLogout = false },
            title = { Text("Sair") },
            text = { Text("Deseja encerrar sua sessao?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogLogout = false
                    viewModel.logout()
                }) {
                    Text("Sair", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogLogout = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Chamados") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.ORDENS) }) {
                        Icon(
                            imageVector = Icons.Filled.Assignment,
                            contentDescription = "Ordens de Servico"
                        )
                    }
                    IconButton(onClick = { mostrarDialogLogout = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnBackground,
                    actionIconContentColor = OnBackground
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.chamados.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                    )
                }

                uiState.erro != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.erro!!,
                            color = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.carregarChamados() },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }

                uiState.chamados.isEmpty() && !uiState.isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoveToInbox,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum chamado encontrado",
                            color = OnSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.chamados) { chamado ->
                            ChamadoItem(
                                chamado = chamado,
                                onClick = {
                                    navController.navigate(Routes.chamadoDetalhe(chamado.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChamadoItem(
    chamado: ChamadoResponse,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = SurfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "#${chamado.id}",
                    fontSize = 11.sp,
                    color = OnSurfaceVariant
                )
                Text(
                    text = chamado.descricao,
                    fontSize = 14.sp,
                    color = OnBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = chamado.local,
                    fontSize = 12.sp,
                    color = OnSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                PrioridadeBadge(prioridade = chamado.prioridade)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chamado.data,
                    fontSize = 11.sp,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PrioridadeBadge(prioridade: String) {
    val (containerColor, contentColor) = when (prioridade.uppercase()) {
        "ALTA" -> ErrorContainer to Error
        "NORMAL" -> TertiaryContainer to Tertiary
        else -> SecondaryContainer to Secondary
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor
    ) {
        Text(
            text = prioridade,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 11.sp,
            color = contentColor
        )
    }
}
