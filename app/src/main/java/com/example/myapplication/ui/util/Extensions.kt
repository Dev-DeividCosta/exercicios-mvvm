package com.example.myapplication.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatCpf(): String {
    val digits = this.filter { it.isDigit() }
    return if (digits.length == 11) {
        "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6, 9)}-${digits.substring(9, 11)}"
    } else {
        this
    }
}

// Data curta (12/10)
fun Long.formatDate(): String {
    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
    return sdf.format(Date(this))
}

// --- NOVO: Data completa (12/10/2024) ---
fun Long.formatFullDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}