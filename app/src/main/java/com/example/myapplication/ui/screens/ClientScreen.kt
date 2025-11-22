package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.utils.MaskVisualTransformation
import com.example.myapplication.ui.utils.PhoneVisualTransformation
import com.example.myapplication.ui.viewmodel.ClientViewModel

@Composable
fun ClientScreen(
    viewModel: ClientViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val cpfMask = remember { MaskVisualTransformation("###.###.###-##") }
    val phoneMask = remember { MaskVisualTransformation("(##) # ####-####") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Novo Cliente",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.client.name,
            onValueChange = { viewModel.onFieldChange("name", it) },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.client.cpf,
            onValueChange = {
                if (it.length <= 11 && it.all { char -> char.isDigit() }) {
                    viewModel.onFieldChange("cpf", it)
                }
            },
            label = { Text("CPF") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = cpfMask,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.client.phone,
            onValueChange = {
                if (it.length <= 11 && it.all { char -> char.isDigit() }) {
                    viewModel.onFieldChange("phone", it)
                }
            },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = phoneMask,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.client.address.city,
            onValueChange = { viewModel.onFieldChange("city", it) },
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.client.address.neighborhood,
            onValueChange = { viewModel.onFieldChange("neighborhood", it) },
            label = { Text("Bairro") },
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
            value = state.client.address.number,
            onValueChange = { viewModel.onFieldChange("number", it) },
            label = { Text("Número") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.complementInput,
            onValueChange = { viewModel.onFieldChange("complement", it) },
            label = { Text("Complemento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.saveClient("user_default") },
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

        if (state.saveSuccess) {
            ClientSuccessAlertDialog(
                onDismiss = { viewModel.resetSuccessState() }
            )
        }
    }
}

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