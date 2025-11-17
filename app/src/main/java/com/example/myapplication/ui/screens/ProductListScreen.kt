package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider // <-- IMPORT ADDED
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview // <-- IMPORT ADDED
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Product
import com.example.myapplication.ui.viewmodel.ProductViewModel
import java.util.UUID // <-- IMPORT ADDED

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            Text(text = "Erro: ${uiState.error}")
        } else {
            ProductListContent(
                products = uiState.productList,
                onAddProductClick = {
                    viewModel.addProduct(
                        Product(
                            id = UUID.randomUUID().toString(), // <-- FIX: Added ID
                            name = "Novo Produto",
                            price = 19.99,
                            descricao = "Descrição do novo produto",
                            urlImagem = ""
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun ProductListContent(
    products: List<Product>,
    onAddProductClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onAddProductClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Adicionar Produto")
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { product ->
                Text(
                    text = "${product.name}: R$ ${product.price}",
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(16.dp)
                )
                HorizontalDivider() // <-- FIX: Renamed from Divider
            }
        }
    }
}

@Preview(showBackground = true) // <-- FIX: Will now resolve
@Composable
fun ProductListContentPreview() {
    // 2. Criamos uma lista "falsa" de produtos
    val fakeProducts = listOf(
        Product(
            id = "1",
            name = "Produto Falso 1",
            price = 10.0,
            descricao = "Descrição legal",
            urlImagem = ""
        ),
        Product(
            id = "2",
            name = "Produto Falso 2",
            price = 20.0,
            descricao = "Outra descrição",
            urlImagem = ""
        ),
        Product(
            id = "3",
            name = "Produto Falso 3",
            price = 30.0,
            descricao = "Descrição final",
            urlImagem = ""
        )
    )

    // 3. Chamamos o Composable "burro" com os dados falsos
    ProductListContent(
        products = fakeProducts,
        onAddProductClick = {} // No preview, o clique não faz nada
    )
}