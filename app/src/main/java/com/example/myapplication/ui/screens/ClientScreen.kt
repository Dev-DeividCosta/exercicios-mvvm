package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.viewmodel.ClientViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

@Composable
fun ClientScreen(
    viewModel: ClientViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Novo Cliente",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nome
        OutlinedTextField(
            value = state.client.name,
            onValueChange = { viewModel.onFieldChange("name", it) },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // CPF
        OutlinedTextField(
            value = state.client.cpf,
            onValueChange = { viewModel.onFieldChange("cpf", it) },
            label = { Text("CPF") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Telefone
        OutlinedTextField(
            value = state.client.phone,
            onValueChange = { viewModel.onFieldChange("phone", it) },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Endereço
        OutlinedTextField(
            value = state.client.address.zipCode,
            onValueChange = { viewModel.onFieldChange("zipCode", it) },
            label = { Text("CEP") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.client.address.city,
            onValueChange = { viewModel.onFieldChange("city", it) },
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.client.address.street,
            onValueChange = { viewModel.onFieldChange("street", it) },
            label = { Text("Rua") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.client.address.houseNumber,
            onValueChange = { viewModel.onFieldChange("houseNumber", it) },
            label = { Text("Número da Casa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.saveClient() },
            // O botão só é habilitado se o form for válido E não estiver carregando
            enabled = state.isFormValid && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isLoading) "Salvando..." else "Salvar Cliente")
        }

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        // --- NOVO ALERT DIALOG PARA SUCESSO ---
        if (state.saveSuccess) {
            ClientSuccessAlertDialog(
                onDismiss = { viewModel.resetSuccessState() }
            )
        }
    }
}

/**
 * AlertDialog elegante para informar sucesso (Heurística de Nielsen).
 */
@Composable
fun ClientSuccessAlertDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(text = "Sucesso!")
        },

        text = {
            Column {
                Text("O novo cliente foi salvo com sucesso.")
            }
        },

        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Continuar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
}