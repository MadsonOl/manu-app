package com.manu.manu_app.ui.screens.chamados

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.manu.manu_app.ui.theme.Error
import com.manu.manu_app.ui.theme.ErrorContainer
import com.manu.manu_app.ui.theme.OnBackground
import com.manu.manu_app.ui.theme.OnSurface
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import com.manu.manu_app.ui.theme.Secondary
import com.manu.manu_app.ui.theme.SecondaryContainer
import com.manu.manu_app.ui.theme.Tertiary
import com.manu.manu_app.ui.theme.TertiaryContainer
import com.manu.manu_app.viewmodel.ChamadoDetalheViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChamadoDetalheScreen(
    navController: NavController,
    chamadoId: String,
    viewModel: ChamadoDetalheViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Chamado #$chamadoId") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                    )
                }

                uiState.erro != null -> {
                    Text(
                        text = uiState.erro!!,
                        color = OnSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.chamado != null -> {
                    val chamado = uiState.chamado!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetalheItem(label = "LOCAL", valor = chamado.local)
                        DetalheItem(label = "DESCRICAO", valor = chamado.descricao)

                        Text(
                            text = "PRIORIDADE",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        PrioridadeBadgeDetalhe(prioridade = chamado.prioridade)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        DetalheItem(label = "SOLICITANTE", valor = chamado.solicitante)
                        DetalheItem(label = "DATA", valor = chamado.data)

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { navController.navigate(Routes.gerarOs(chamado.id)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Gerar Ordem de Servico")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetalheItem(label: String, valor: String) {
    Text(
        text = label,
        fontSize = 11.sp,
        color = OnSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = valor,
        fontSize = 15.sp,
        color = OnSurface
    )
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
private fun PrioridadeBadgeDetalhe(prioridade: String) {
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
