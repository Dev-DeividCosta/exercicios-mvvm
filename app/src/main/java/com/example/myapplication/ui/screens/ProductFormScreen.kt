package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Product
import com.example.myapplication.ui.viewmodel.ProductViewModel

/**
 * Tela que exibe o formulário para adicionar produtos
 * e a lista de produtos cadastrados.
 */
@Composable
fun ProductFormScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Formulário de produtos
        ProductFormScreen(
            onProductSave = { product ->
                viewModel.addProduct(product)
            }
        )

        // Lista de produtos
        uiState.productList.forEach { product ->
            Text(
                text = "• ${product.name} - R$${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (uiState.isLoading) {
            Text(
                text = "Carregando produtos...",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        uiState.error?.let { error ->
            Text(
                text = "Erro: $error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Formulário para criar ou editar um produto.
 */
@Composable
fun ProductFormScreen(
    onProductSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Adicionar Novo Produto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Preço (ex: 19.99)") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL da Imagem") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val newProduct = Product(
                    id = "",
                    name = name,
                    price = price.toDoubleOrNull() ?: 0.0,
                    description = description,
                    imageUrl = imageUrl
                )
                onProductSave(newProduct)
                // Limpa campos após salvar
                name = ""
                price = ""
                description = ""
                imageUrl = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Produto")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductFormScreenPreview() {
    ProductFormScreen()
}
