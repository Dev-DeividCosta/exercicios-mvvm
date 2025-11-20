// PACOTE: com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Charges : BottomNavItem(
        route = "charges",
        title = "Cobranças",
        icon = Icons.Filled.Home
    )

    object Sales : BottomNavItem(
        route = "sales",
        title = "Vendas",
        icon = Icons.Filled.ShoppingCart
    )

    object Clients : BottomNavItem(
        route = "clients",
        title = "Clientes",
        icon = Icons.Filled.Person
    )

    object Products : BottomNavItem(
        route = "products",
        title = "Produtos",
        icon = Icons.Filled.List
    )
}
