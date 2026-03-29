package com.manu.manu_app.ui.screens.solicitante

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.manu.manu_app.ui.theme.Background
import com.manu.manu_app.ui.theme.ErrorContainer
import com.manu.manu_app.ui.theme.OnBackground
import com.manu.manu_app.ui.theme.OnError
import com.manu.manu_app.ui.theme.OnSurfaceVariant
import com.manu.manu_app.ui.theme.Primary
import com.manu.manu_app.ui.theme.Secondary
import com.manu.manu_app.ui.theme.SecondaryContainer
import com.manu.manu_app.ui.theme.SurfaceVariant
import com.manu.manu_app.ui.theme.Tertiary
import com.manu.manu_app.viewmodel.SolicitanteViewModel

fun obterEndereco(context: android.content.Context, lat: Double, lng: Double): String {
    return try {
        val geocoder = android.location.Geocoder(context, java.util.Locale("pt", "BR"))
        val resultados = geocoder.getFromLocation(lat, lng, 1)
        if (!resultados.isNullOrEmpty()) {
            val endereco = resultados[0]
            val partes = mutableListOf<String>()
            endereco.thoroughfare?.let { partes.add(it) }
            endereco.subThoroughfare?.let { partes.add(it) }
            endereco.subLocality?.let { partes.add(it) }
            endereco.subAdminArea?.let { partes.add(it) }
            if (partes.isEmpty()) {
                endereco.getAddressLine(0) ?: "Lat: $lat, Lng: $lng"
            } else {
                partes.joinToString(", ")
            }
        } else {
            "Lat: $lat, Lng: $lng"
        }
    } catch (e: Exception) {
        "Lat: $lat, Lng: $lng"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitanteScreen(
    navController: NavController,
    viewModel: SolicitanteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            try {
                fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val endereco = obterEndereco(context, location.latitude, location.longitude)
                            viewModel.onLocalizacaoCapturada(location.latitude, location.longitude, endereco)
                        } else {
                            viewModel.onGpsFalhou()
                        }
                    }
                    .addOnFailureListener { viewModel.onGpsFalhou() }
            } catch (_: SecurityException) {
                viewModel.onGpsFalhou()
            }
        } else {
            viewModel.onGpsFalhou()
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            try {
                fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val endereco = obterEndereco(context, location.latitude, location.longitude)
                            viewModel.onLocalizacaoCapturada(location.latitude, location.longitude, endereco)
                        } else {
                            viewModel.onGpsFalhou()
                        }
                    }
                    .addOnFailureListener { viewModel.onGpsFalhou() }
            } catch (_: SecurityException) {
                viewModel.onGpsFalhou()
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Abrir Chamado") },
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // GPS Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        uiState.gpsStatus.contains("capturada") -> {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = uiState.gpsStatus, color = Secondary)
                        }
                        uiState.gpsStatus.contains("nao disponivel") -> {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = uiState.gpsStatus, color = Tertiary)
                        }
                        else -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = uiState.gpsStatus, color = OnSurfaceVariant)
                        }
                    }
                }
            }

            // Local
            OutlinedTextField(
                value = uiState.local,
                onValueChange = { viewModel.onLocalChange(it) },
                label = { Text("Local") },
                modifier = Modifier.fillMaxWidth()
            )

            // Descricao
            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = { viewModel.onDescricaoChange(it) },
                label = { Text("Descricao") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            // Solicitante
            OutlinedTextField(
                value = uiState.solicitante,
                onValueChange = { viewModel.onSolicitanteChange(it) },
                label = { Text("Solicitante") },
                modifier = Modifier.fillMaxWidth()
            )

            // Prioridade Dropdown
            var expanded by remember { mutableStateOf(false) }
            val opcoes = listOf("BAIXA", "NORMAL", "ALTA")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.prioridade,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridade") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    opcoes.forEach { opcao ->
                        DropdownMenuItem(
                            text = { Text(opcao) },
                            onClick = {
                                viewModel.onPrioridadeChange(opcao)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Erro
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
            }

            // Sucesso
            if (uiState.sucesso) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SecondaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Chamado enviado com sucesso!",
                                color = Secondary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.resetSucesso() },
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                        ) {
                            Text("Abrir novo chamado")
                        }
                    }
                }
            }

            // Botao Enviar
            Button(
                onClick = { viewModel.enviarChamado() },
                modifier = Modifier.fillMaxWidth(),
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
                    Text("Enviar Chamado")
                }
            }
        }
    }
}
