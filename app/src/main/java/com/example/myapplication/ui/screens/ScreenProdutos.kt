package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Product
import com.example.myapplication.ui.viewmodel.ProductViewModel
import com.example.myapplication.ui.viewmodel.ProductsUiState
import com.example.myapplication.util.toBRL

/**
 * Composable para a tela principal de listagem de produtos.
 * @param viewModel Injetado pelo Hilt.
 * @param onNavigateToProductDetail Função de callback para navegação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProdutos(
    viewModel: ProductViewModel = hiltViewModel(),
    onNavigateToProductDetail: (String?) -> Unit
) {
    // 1. Coleta o estado da UI como um Compose State
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Estoque de Produtos") })
        },
        // FLOATING ACTION BUTTON REMOVIDO DAQUI
    ) { paddingValues ->
        // 2. Chama a função de renderização de acordo com o estado
        ProductsContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onProductClick = { productId -> onNavigateToProductDetail(productId) }
        )
    }
}

/**
 * Componente que lida com os diferentes estados de UI.
 */
@Composable
private fun ProductsContent(
    modifier: Modifier = Modifier,
    uiState: ProductsUiState,
    onProductClick: (String) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            // 2a. Estado de Carregamento
            ProductsUiState.Loading -> {
                CircularProgressIndicator()
            }

            // 2b. Estado de Erro
            is ProductsUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 2c. Estado de Sucesso
            is ProductsUiState.Success -> {
                if (uiState.products.isEmpty()) {
                    Text("Nenhum produto em estoque.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.products, key = { it.id }) { product ->
                            ProductItem(product = product, onClick = { onProductClick(product.id) })
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable para cada item da lista de produtos (Card de visualização).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nome do Produto (Título)
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Preço Formatado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Preço:", fontWeight = FontWeight.SemiBold)
                Text(
                    text = product.price.toBRL(),
                    fontWeight = FontWeight.Medium
                )
            }

            // Quantidade em Estoque
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Estoque:", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "${product.stockQuantity} un.",
                    fontWeight = FontWeight.Medium,
                    // Destaca se o estoque estiver baixo (ex: < 10)
                    color = if (product.stockQuantity < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }

            // Descrição
            if (product.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Descrição: ${product.description}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}