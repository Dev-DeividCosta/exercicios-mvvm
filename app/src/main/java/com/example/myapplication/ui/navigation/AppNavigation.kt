////package com.example.myapplication.ui.navigation
////
////import androidx.compose.foundation.layout.padding
////import androidx.compose.material3.Scaffold
////import androidx.compose.runtime.Composable
////import androidx.compose.runtime.remember
////import androidx.compose.ui.Modifier
////import androidx.navigation.compose.NavHost
////import androidx.navigation.compose.composable
////import androidx.navigation.compose.rememberNavController
////import com.example.myapplication.ui.screens.ClientScreen
////import com.example.myapplication.ui.screen.ScreenProdutos
////
////@Composable
////fun AppNavigator() {
////    val navController = rememberNavController()
////    val bottomNavItems = remember {
////        listOf(
////            BottomNavItem.Charges,
////            BottomNavItem.Sales,
////            BottomNavItem.Clients,
////            BottomNavItem.Products
////        )
////    }
////
////    Scaffold(
////        bottomBar = {
////            AppBottomNavigationBar(navController, bottomNavItems)
////        }
////    ) { paddingValues ->
////        NavHost(
////            navController = navController,
////            startDestination = BottomNavItem.Clients.route,
////            modifier = Modifier.padding(paddingValues)
////        ) {
////            composable(BottomNavItem.Clients.route) {
////                ClientScreen()
////            }
////            composable(BottomNavItem.Products.route) {
////                ScreenProdutos(onNavigateToProductDetail = {})
////            }
////            composable(BottomNavItem.Sales.route) {
////                PlaceholderScreen(BottomNavItem.Sales.title)
////            }
////            composable(BottomNavItem.Charges.route) {
////                PlaceholderScreen(BottomNavItem.Charges.title)
////            }
////        }
////    }
////}
//
//
//
//package com.example.myapplication.ui.navigation
//
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import com.example.myapplication.ui.screens.ClientScreen
//import com.example.myapplication.ui.screen.ScreenProdutos
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
//@Composable
//fun AppNavigator() {
//    val items = remember {
//        listOf(
//            BottomNavItem.Charges,
//            BottomNavItem.Sales,
//            BottomNavItem.Clients,
//            BottomNavItem.Products
//        )
//    }
//
//    val pagerState = rememberPagerState(
//        initialPage = items.indexOf(BottomNavItem.Clients),
//        pageCount = { items.size }
//    )
//    val scope = rememberCoroutineScope()
//
//    Scaffold(
//        bottomBar = {
//            HorizontalBarForPager(
//                items = items,
//                selectedPageIndex = pagerState.currentPage,
//                onTabSelected = { index ->
//                    scope.launch {
//                        pagerState.scrollToPage(index)
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.fillMaxSize().padding(paddingValues),
//            key = { index -> items[index].route }
//        ) { pageIndex ->
//            when (items[pageIndex]) {
//                BottomNavItem.Clients -> ClientScreen()
//                BottomNavItem.Products -> ScreenProdutos(onNavigateToProductDetail = {})
//                BottomNavItem.Sales -> PlaceholderScreen(BottomNavItem.Sales.title)
//                BottomNavItem.Charges -> PlaceholderScreen(BottomNavItem.Charges.title)
//            }
//        }
//    }
//}
//
//@Composable
//fun HorizontalBarForPager(
//    items: List<BottomNavItem>,
//    selectedPageIndex: Int,
//    onTabSelected: (Int) -> Unit
//) {
//    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.surfaceContainer
//    ) {
//        items.forEachIndexed { index, item ->
//            NavigationBarItem(
//                icon = { Icon(item.icon, contentDescription = item.title) },
//                label = { Text(item.title) },
//                selected = index == selectedPageIndex,
//                onClick = { onTabSelected(index) }
//            )
//        }
//    }
//}

package com.example.myapplication.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
// IMPORTS DAS TELAS
import com.example.myapplication.ui.screens.ClientScreen
import com.example.myapplication.ui.screens.SaleScreen
import com.example.myapplication.ui.screen.ScreenProdutos
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppNavigator() {
    val items = remember {
        listOf(
            BottomNavItem.Charges,
            BottomNavItem.Sales,
            BottomNavItem.Clients,
            BottomNavItem.Products
        )
    }

    val pagerState = rememberPagerState(
        initialPage = items.indexOf(BottomNavItem.Sales), // Inicia na tela de Vendas
        pageCount = { items.size }
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            HorizontalBarForPager(
                items = items,
                selectedPageIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            key = { index -> items[index].route },
            // Desativa o scroll lateral manual para não atrapalhar o formulário de vendas
            userScrollEnabled = false
        ) { pageIndex ->
            when (items[pageIndex]) {
                BottomNavItem.Clients -> ClientScreen()
                BottomNavItem.Products -> ScreenProdutos(onNavigateToProductDetail = {})
                BottomNavItem.Sales -> SaleScreen() // <-- CONECTADO AQUI
                BottomNavItem.Charges -> PlaceholderScreen(BottomNavItem.Charges.title)
            }
        }
    }
}

@Composable
fun HorizontalBarForPager(
    items: List<BottomNavItem>,
    selectedPageIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = index == selectedPageIndex,
                onClick = { onTabSelected(index) }
            )
        }
    }
}