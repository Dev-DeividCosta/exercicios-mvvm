package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta final (sem modo escuro, sem Dynamic Color)
private val AppColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryGrey,
    tertiary = TertiaryBlueGrey,
    background = BackgroundWhite,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {

    // --- INÍCIO DA CORREÇÃO DA BARRA DE STATUS ---
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 1. Define a cor da barra de status para a cor de fundo do seu app.
            // Isso garante uma transição suave entre a UI do app e a barra.
            window.statusBarColor = AppColorScheme.background.toArgb()

            // 2. O ponto principal: Força os ícones (horas, notificação) a serem escuros (PRETO).
            // isAppearanceLightStatusBars = true significa: "o fundo é CLARO, então use ícones ESCUROS".
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    // --- FIM DA CORREÇÃO DA BARRA DE STATUS ---

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography, // Assumindo que Typography está definido em outro lugar neste pacote
        content = content
    )
}