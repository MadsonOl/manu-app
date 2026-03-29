package com.manu.manu_app.ui.screens.empresas

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.manu.manu_app.data.model.Empresa
import com.manu.manu_app.ui.theme.Background
import com.manu.manu_app.ui.theme.Error
import com.manu.manu_app.ui.theme.OnBackground
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import com.manu.manu_app.ui.theme.Secondary
import com.manu.manu_app.ui.theme.SurfaceVariant
import com.manu.manu_app.viewmodel.EmpresasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpresasScreen(
    navController: NavController,
    viewModel: EmpresasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var empresaParaDeletar by remember { mutableStateOf<Empresa?>(null) }

    LaunchedEffect(uiState.sucesso) {
        uiState.sucesso?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagens()
        }
    }

    LaunchedEffect(uiState.erro) {
        uiState.erro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagens()
        }
    }

    if (empresaParaDeletar != null) {
        AlertDialog(
            onDismissRequest = { empresaParaDeletar = null },
            title = { Text("Remover empresa") },
            text = { Text("Deseja remover \"${empresaParaDeletar!!.nome}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletar(empresaParaDeletar!!.id)
                    empresaParaDeletar = null
                }) {
                    Text("Remover", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { empresaParaDeletar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (uiState.mostrarFormulario) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.fecharFormulario() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (uiState.editandoId != null) "Editar Empresa" else "Nova Empresa",
                    fontSize = 20.sp,
                    color = OnBackground
                )

                OutlinedTextField(
                    value = uiState.cnpj,
                    onValueChange = { viewModel.onCnpjChange(it) },
                    label = { Text("CNPJ") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.nome,
                    onValueChange = { viewModel.onNomeChange(it) },
                    label = { Text("Nome da Empresa") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.endereco,
                    onValueChange = { viewModel.onEnderecoChange(it) },
                    label = { Text("Endereco") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.gestor,
                    onValueChange = { viewModel.onGestorChange(it) },
                    label = { Text("Gestor de Manutencao") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.info,
                    onValueChange = { viewModel.onInfoChange(it) },
                    label = { Text("Informacoes Adicionais") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.fecharFormulario() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { viewModel.salvar() },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = OnBackground
                            )
                        } else {
                            Text("Salvar")
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Empresas") },
                actions = {
                    IconButton(onClick = { viewModel.abrirFormulario() }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Adicionar empresa"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnBackground,
                    actionIconContentColor = OnBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val isError = uiState.erro != null
                Snackbar(
                    snackbarData = data,
                    containerColor = if (isError) Error else Secondary
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.empresas.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                    )
                }

                uiState.erro != null && uiState.empresas.isEmpty() -> {
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
                            onClick = { viewModel.carregarEmpresas() },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }

                uiState.empresas.isEmpty() && !uiState.isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Business,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhuma empresa cadastrada",
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
                        items(uiState.empresas) { empresa ->
                            EmpresaItem(
                                empresa = empresa,
                                onEditar = { viewModel.iniciarEdicao(empresa) },
                                onDeletar = { empresaParaDeletar = empresa }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmpresaItem(
    empresa: Empresa,
    onEditar: () -> Unit,
    onDeletar: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = SurfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = empresa.nome,
                fontSize = 15.sp,
                color = OnBackground
            )
            Text(
                text = empresa.cnpj,
                fontSize = 13.sp,
                color = Primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = empresa.endereco,
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )
            Text(
                text = "Gestor: ${empresa.gestor_manutencao}",
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEditar) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onDeletar) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Deletar",
                        tint = Error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
