package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContadorViewModel : ViewModel() {
    private val _contador = MutableStateFlow(0)

    val contador: StateFlow<Int> = _contador.asStateFlow()

    fun incrementar() {
        _contador.update { valorAtual ->
            valorAtual + 1
        }
    }
}