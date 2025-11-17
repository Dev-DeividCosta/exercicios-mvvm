package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myapplication.domain.model.Product
import com.example.myapplication.ui.screens.ProductFormScreen
import com.example.myapplication.ui.screens.ProductListScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ProductViewModel = hiltViewModel()

                    // Estado para alternar entre a lista e o formulário
                    var showForm by remember { mutableStateOf(false) }

                    if (showForm) {
                        ProductFormScreen(
                            onProductSave = { product: Product ->
                                viewModel.addProduct(product)
                                showForm = false // Volta para a lista
                            }
                        )
                    } else {
                        ProductListScreen(
                            viewModel = viewModel,
                            onAddProductClick = { showForm = true }
                        )
                    }
                }
            }
        }
    }
}
