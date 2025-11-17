package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Product
import com.example.myapplication.ui.viewmodel.ProductViewModel
import java.util.UUID

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onAddProductClick: () -> Unit // Novo parâmetro para abrir o formulário
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
                onAddProductClick = onAddProductClick
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
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListContentPreview() {
    val fakeProducts = listOf(
        Product(
            id = "1",
            name = "Produto Falso 1",
            price = 10.0,
            description = "Descrição legal",
            imageUrl = ""
        ),
        Product(
            id = "2",
            name = "Produto Falso 2",
            price = 20.0,
            description = "Outra descrição",
            imageUrl = ""
        ),
        Product(
            id = "3",
            name = "Produto Falso 3",
            price = 30.0,
            description = "Descrição final",
            imageUrl = ""
        )
    )

    ProductListContent(
        products = fakeProducts,
        onAddProductClick = {}
    )
}
