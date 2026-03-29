package com.manu.manu_app.ui.screens.ordens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.manu.manu_app.ui.navigation.Routes
import com.manu.manu_app.ui.theme.Background
import com.manu.manu_app.ui.theme.ErrorContainer
import com.manu.manu_app.ui.theme.OnBackground
import com.manu.manu_app.ui.theme.OnError
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import com.manu.manu_app.ui.theme.Secondary
import com.manu.manu_app.ui.theme.SecondaryContainer
import com.manu.manu_app.ui.theme.SurfaceVariant
import com.manu.manu_app.viewmodel.GerarOsEvent
import com.manu.manu_app.viewmodel.GerarOsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerarOsScreen(
    navController: NavController,
    chamadoId: String,
    viewModel: GerarOsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GerarOsEvent.Sucesso -> {
                    delay(1500)
                    navController.navigate(Routes.CHAMADOS) {
                        popUpTo(Routes.CHAMADOS) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Nova Ordem de Servico") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnBackground,
                    navigationIconContentColor = OnBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.chamado != null) {
                val chamado = uiState.chamado!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "LOCAL", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text(text = chamado.local, fontSize = 14.sp, color = OnBackground)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "DESCRICAO", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text(text = chamado.descricao, fontSize = 14.sp, color = OnBackground)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "PRIORIDADE", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text(text = chamado.prioridade, fontSize = 14.sp, color = OnBackground)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "SOLICITANTE", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text(text = chamado.solicitante, fontSize = 14.sp, color = OnBackground)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.responsavel,
                onValueChange = { viewModel.onResponsavelChange(it) },
                label = { Text("Responsavel") },
                placeholder = { Text("Nome do profissional responsavel") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.erro != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ErrorContainer)
                ) {
                    Text(
                        text = uiState.erro!!,
                        modifier = Modifier.padding(16.dp),
                        color = OnError
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.sucesso) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SecondaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ordem de servico criada com sucesso!",
                            color = Secondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.criarOrdem() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && !uiState.sucesso,
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = OnBackground
                    )
                } else {
                    Text("Cadastrar OS")
                }
            }
        }
    }
}
