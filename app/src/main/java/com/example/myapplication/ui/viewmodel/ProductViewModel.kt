package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        // Carrega produtos assim que o ViewModel nasce
        viewModelScope.launch {
            repository.getAllProducts().collect {
                _products.value = it
            }
        }
    }

    // Exemplo de como adicionar produto (se precisar criar uma tela para isso depois)
    fun addProduct(name: String, price: Double, description: String, stock: Int) {
        viewModelScope.launch {
            val prod = Product(id = "", name = name, price = price, description = description, stockQuantity = stock)
            repository.createProduct(prod)
        }
    }
}