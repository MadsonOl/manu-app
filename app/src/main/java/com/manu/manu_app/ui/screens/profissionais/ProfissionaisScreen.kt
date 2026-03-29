package com.manu.manu_app.ui.screens.profissionais

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
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
import com.manu.manu_app.data.model.Profissional
import com.manu.manu_app.ui.theme.Background
import com.manu.manu_app.ui.theme.Error
import com.manu.manu_app.ui.theme.OnBackground
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import com.manu.manu_app.ui.theme.Secondary
import com.manu.manu_app.ui.theme.SurfaceVariant
import com.manu.manu_app.viewmodel.ProfissionaisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfissionaisScreen(
    navController: NavController,
    viewModel: ProfissionaisViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var profissionalParaDeletar by remember { mutableStateOf<Profissional?>(null) }

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

    // Dialog de confirmacao de exclusao
    if (profissionalParaDeletar != null) {
        AlertDialog(
            onDismissRequest = { profissionalParaDeletar = null },
            title = { Text("Remover profissional") },
            text = { Text("Deseja remover \"${profissionalParaDeletar!!.nome}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletar(profissionalParaDeletar!!.id)
                    profissionalParaDeletar = null
                }) {
                    Text("Remover", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { profissionalParaDeletar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Bottom sheet formulario profissional
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
                    text = if (uiState.editandoId != null) "Editar Profissional" else "Novo Profissional",
                    fontSize = 20.sp,
                    color = OnBackground
                )

                OutlinedTextField(
                    value = uiState.nome,
                    onValueChange = { viewModel.onNomeChange(it) },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.telefone,
                    onValueChange = { viewModel.onTelefoneChange(it) },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.rg,
                    onValueChange = { viewModel.onRgChange(it) },
                    label = { Text("RG") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.cpf,
                    onValueChange = { viewModel.onCpfChange(it) },
                    label = { Text("CPF") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Dropdown de funcao
                var funcaoExpanded by remember { mutableStateOf(false) }
                val funcaoNome = uiState.funcoes.find { it.id == uiState.funcaoSelecionada }?.nome ?: ""

                ExposedDropdownMenuBox(
                    expanded = funcaoExpanded,
                    onExpandedChange = { funcaoExpanded = it }
                ) {
                    OutlinedTextField(
                        value = funcaoNome,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Funcao") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = funcaoExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = funcaoExpanded,
                        onDismissRequest = { funcaoExpanded = false }
                    ) {
                        uiState.funcoes.forEach { funcao ->
                            DropdownMenuItem(
                                text = { Text(funcao.nome) },
                                onClick = {
                                    viewModel.onFuncaoChange(funcao.id)
                                    funcaoExpanded = false
                                }
                            )
                        }
                    }
                }

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

    // Bottom sheet formulario funcao
    if (uiState.mostrarFormularioFuncao) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.fecharFormularioFuncao() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Nova Funcao",
                    fontSize = 20.sp,
                    color = OnBackground
                )

                OutlinedTextField(
                    value = uiState.nomeFuncao,
                    onValueChange = { viewModel.onNomeFuncaoChange(it) },
                    label = { Text("Nome da Funcao") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.fecharFormularioFuncao() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { viewModel.salvarFuncao() },
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
                title = { Text("Profissionais") },
                actions = {
                    IconButton(onClick = { viewModel.abrirFormularioFuncao() }) {
                        Icon(
                            imageVector = Icons.Filled.GroupAdd,
                            contentDescription = "Cadastrar funcao"
                        )
                    }
                    IconButton(onClick = { viewModel.abrirFormulario() }) {
                        Icon(
                            imageVector = Icons.Filled.PersonAdd,
                            contentDescription = "Cadastrar profissional"
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
                uiState.isLoading && uiState.profissionais.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                    )
                }

                uiState.erro != null && uiState.profissionais.isEmpty() -> {
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
                            onClick = { viewModel.carregarDados() },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }

                uiState.profissionais.isEmpty() && !uiState.isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum profissional cadastrado",
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
                        items(uiState.profissionais) { profissional ->
                            ProfissionalItem(
                                profissional = profissional,
                                onEditar = { viewModel.iniciarEdicao(profissional) },
                                onDeletar = { profissionalParaDeletar = profissional }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfissionalItem(
    profissional: Profissional,
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
                text = profissional.nome,
                fontSize = 15.sp,
                color = OnBackground
            )
            Text(
                text = profissional.funcao ?: "Sem funcao",
                fontSize = 13.sp,
                color = Primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = profissional.telefone,
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )
            Text(
                text = profissional.email,
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
