// File: app/src/main/java/com/example/myapplication/util/CurrencyFormatter.kt

package com.example.myapplication.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Função de extensão para formatar um Double como moeda brasileira (Real - R$).
 */
fun Double.toBRL(): String {
    // Define a localização para o Brasil
    val localeBR = Locale("pt", "BR")

    // Obtém um formatador de moeda para a localização especificada
    val currencyFormatter = NumberFormat.getCurrencyInstance(localeBR)

    // Formata o valor
    return currencyFormatter.format(this)
}