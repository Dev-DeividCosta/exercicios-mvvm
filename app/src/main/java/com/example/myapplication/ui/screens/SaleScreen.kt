//package com.example.myapplication.ui.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.myapplication.domain.model.Client
//import com.example.myapplication.domain.model.Product
//import com.example.myapplication.domain.model.SaleItem
//import com.example.myapplication.ui.viewmodel.SaleViewModel
//
//@Composable
//fun SaleScreen(
//    viewModel: SaleViewModel = hiltViewModel()
//) {
//    val state by viewModel.uiState.collectAsState()
//    val scrollState = rememberScrollState() // Para permitir rolar a tela se for longa
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(scrollState), // Habilita rolagem
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Nova Venda",
//            style = MaterialTheme.typography.headlineSmall
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- SELEÇÃO DE CLIENTE ---
//        Text("Cliente", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
//        ClientSelector(
//            clients = state.availableClients,
//            selectedClientName = state.currentSale.clientName,
//            onClientSelected = { viewModel.selectClient(it) }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- ADICIONAR PRODUTOS (Área de Ação) ---
//        Card(
//            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                Text("Adicionar Item", style = MaterialTheme.typography.titleSmall)
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Componente auxiliar para escolher produto e quantidade
//                ProductAdder(
//                    products = state.availableProducts,
//                    onAddProduct = { product, qty ->
//                        viewModel.addProductToSale(product, qty)
//                    }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- LISTA DE ITENS (CARRINHO) ---
//        if (state.currentSale.items.isNotEmpty()) {
//            Text("Itens da Venda", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
//            Spacer(modifier = Modifier.height(4.dp))
//
//            // Lista simples dos itens já adicionados
//            state.currentSale.items.forEachIndexed { index, item ->
//                SaleItemRow(
//                    item = item,
//                    onRemove = { viewModel.removeItem(index) }
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//            }
//
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//            // Totalizador
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("TOTAL:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//                Text(
//                    "R$ ${String.format("%.2f", state.currentSale.totalValue)}",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- DETALHES FINAIS ---
//        OutlinedTextField(
//            value = if (state.currentSale.installments == 0) "" else state.currentSale.installments.toString(),
//            onValueChange = {
//                // Tenta converter para Int, se falhar usa 1
//                val number = it.toIntOrNull() ?: 1
//                viewModel.updateInstallments(number)
//            },
//            label = { Text("Parcelas") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = state.currentSale.observation,
//            onValueChange = { viewModel.updateObservation(it) },
//            label = { Text("Observação") },
//            modifier = Modifier.fillMaxWidth(),
//            maxLines = 3
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // --- BOTÃO SALVAR ---
//        Button(
//            onClick = { viewModel.saveSale() },
//            enabled = state.currentSale.items.isNotEmpty() &&
//                    state.currentSale.clientId.isNotBlank() &&
//                    !state.isLoading,
//            modifier = Modifier.fillMaxWidth().height(50.dp)
//        ) {
//            if (state.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(24.dp),
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Processando...")
//            } else {
//                Text("Finalizar Venda")
//            }
//        }
//
//        if (state.errorMessage != null) {
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = state.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
//        }
//
//        // --- DIALOG DE SUCESSO ---
//        if (state.saveSuccess) {
//            SaleSuccessAlertDialog(onDismiss = { viewModel.resetSuccessState() })
//        }
//    }
//}
//
//// --------------------------------------------------------
//// COMPONENTES AUXILIARES (Para limpar o código principal)
//// --------------------------------------------------------
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ClientSelector(
//    clients: List<Client>,
//    selectedClientName: String,
//    onClientSelected: (Client) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        OutlinedTextField(
//            value = selectedClientName.ifBlank { "Selecione um cliente" },
//            onValueChange = {},
//            readOnly = true,
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            modifier = Modifier.menuAnchor().fillMaxWidth()
//        )
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            clients.forEach { client ->
//                DropdownMenuItem(
//                    text = { Text(client.name) },
//                    onClick = {
//                        onClientSelected(client)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductAdder(
//    products: List<Product>,
//    onAddProduct: (Product, Int) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedProduct by remember { mutableStateOf<Product?>(null) }
//    var quantityText by remember { mutableStateOf("1") }
//
//    Column {
//        // Dropdown de Produtos
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = !expanded }
//        ) {
//            OutlinedTextField(
//                value = selectedProduct?.name ?: "Selecione o Produto",
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor().fillMaxWidth()
//            )
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                products.forEach { product ->
//                    DropdownMenuItem(
//                        text = { Text("${product.name} (R$ ${product.price})") },
//                        onClick = {
//                            selectedProduct = product
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            // Campo Quantidade
//            OutlinedTextField(
//                value = quantityText,
//                onValueChange = { quantityText = it },
//                label = { Text("Qtd") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.weight(1f)
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            // Botão Adicionar
//            Button(
//                onClick = {
//                    selectedProduct?.let { prod ->
//                        val qty = quantityText.toIntOrNull() ?: 1
//                        if (qty > 0) {
//                            onAddProduct(prod, qty)
//                            // Reseta para o próximo item
//                            selectedProduct = null
//                            quantityText = "1"
//                        }
//                    }
//                },
//                enabled = selectedProduct != null
//            ) {
//                Icon(Icons.Default.Add, contentDescription = null)
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Add")
//            }
//        }
//    }
//}
//
//@Composable
//fun SaleItemRow(item: SaleItem, onRemove: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(text = item.productName, fontWeight = FontWeight.Bold)
//            Text(
//                text = "${item.quantity}x R$ ${item.unitPrice} = R$ ${item.subtotal}",
//                style = MaterialTheme.typography.bodySmall
//            )
//        }
//        IconButton(onClick = onRemove) {
//            Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Red)
//        }
//    }
//}
//
//@Composable
//fun SaleSuccessAlertDialog(onDismiss: () -> Unit) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
//        title = { Text(text = "Venda Realizada!") },
//        text = { Text("A venda foi salva com sucesso e sincronizada.") },
//        confirmButton = {
//            Button(onClick = onDismiss) {
//                Text("Nova Venda")
//            }
//        },
//        containerColor = MaterialTheme.colorScheme.surfaceVariant
//    )
//}


//package com.example.myapplication.ui.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.myapplication.domain.model.Client
//import com.example.myapplication.domain.model.Product
//import com.example.myapplication.domain.model.SaleItem
//import com.example.myapplication.ui.viewmodel.SaleViewModel
//
//@Composable
//fun SaleScreen(
//    viewModel: SaleViewModel = hiltViewModel()
//) {
//    val state by viewModel.uiState.collectAsState()
//    val scrollState = rememberScrollState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(scrollState),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Nova Venda",
//            style = MaterialTheme.typography.headlineSmall
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- SELEÇÃO DE CLIENTE ---
//        Text("Cliente", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
//
//        ClientSelector(
//            clients = state.availableClients,
//            selectedClientName = state.currentSale.clientName,
//            onClientSelected = { viewModel.selectClient(it) },
//            onClientClear = { viewModel.clearClient() } // Função para desmarcar
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- ADICIONAR PRODUTOS (Área de Ação) ---
//        Card(
//            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                Text("Adicionar Item", style = MaterialTheme.typography.titleSmall)
//                Spacer(modifier = Modifier.height(8.dp))
//
//                ProductAdder(
//                    products = state.availableProducts,
//                    onAddProduct = { product, qty ->
//                        viewModel.addProductToSale(product, qty)
//                    }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- LISTA DE ITENS (CARRINHO) ---
//        if (state.currentSale.items.isNotEmpty()) {
//            Text("Itens da Venda", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
//            Spacer(modifier = Modifier.height(4.dp))
//
//            state.currentSale.items.forEachIndexed { index, item ->
//                SaleItemRow(
//                    item = item,
//                    onRemove = { viewModel.removeItem(index) }
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//            }
//
//            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
//
//            // Totalizador
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("TOTAL:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//                Text(
//                    "R$ ${String.format("%.2f", state.currentSale.totalValue)}",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- DETALHES FINAIS ---
//        OutlinedTextField(
//            value = if (state.currentSale.installments == 0) "" else state.currentSale.installments.toString(),
//            onValueChange = {
//                val number = it.toIntOrNull() ?: 1
//                viewModel.updateInstallments(number)
//            },
//            label = { Text("Parcelas") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = state.currentSale.observation,
//            onValueChange = { viewModel.updateObservation(it) },
//            label = { Text("Observação") },
//            modifier = Modifier.fillMaxWidth(),
//            maxLines = 3
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // --- BOTÃO SALVAR ---
//        Button(
//            onClick = { viewModel.saveSale() },
//            enabled = state.currentSale.items.isNotEmpty() &&
//                    state.currentSale.clientId.isNotBlank() &&
//                    !state.isLoading,
//            modifier = Modifier.fillMaxWidth().height(50.dp)
//        ) {
//            if (state.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(24.dp),
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Processando...")
//            } else {
//                Text("Finalizar Venda")
//            }
//        }
//
//        if (state.errorMessage != null) {
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = state.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
//        }
//
//        // --- DIALOG DE SUCESSO ---
//        if (state.saveSuccess) {
//            SaleSuccessAlertDialog(onDismiss = { viewModel.resetSuccessState() })
//        }
//    }
//}
//
//// --------------------------------------------------------
//// COMPONENTES AUXILIARES
//// --------------------------------------------------------
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ClientSelector(
//    clients: List<Client>,
//    selectedClientName: String,
//    onClientSelected: (Client) -> Unit,
//    onClientClear: () -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        OutlinedTextField(
//            value = selectedClientName.ifBlank { "Selecione um cliente" },
//            onValueChange = {},
//            readOnly = true,
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            modifier = Modifier.menuAnchor().fillMaxWidth(),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedTextColor = if (selectedClientName.isBlank()) Color.Gray else Color.Black,
//                focusedTextColor = if (selectedClientName.isBlank()) Color.Gray else Color.Black
//            )
//        )
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            // --- OPÇÃO DE DESMARCAR ---
//            // Só aparece se já tiver um cliente selecionado
//            if (selectedClientName.isNotBlank()) {
//                DropdownMenuItem(
//                    text = { Text("Remover seleção", color = MaterialTheme.colorScheme.error) },
//                    onClick = {
//                        onClientClear()
//                        expanded = false
//                    }
//                )
//                HorizontalDivider()
//            }
//
//            // --- LISTA DE CLIENTES ---
//            clients.forEach { client ->
//                DropdownMenuItem(
//                    text = {
//                        Column {
//                            Text(text = client.name, style = MaterialTheme.typography.bodyLarge)
//                            // Aqui mostramos o CPF
//                            Text(
//                                text = "CPF: ${client.cpf}",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color.Gray
//                            )
//                        }
//                    },
//                    onClick = {
//                        onClientSelected(client)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductAdder(
//    products: List<Product>,
//    onAddProduct: (Product, Int) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedProduct by remember { mutableStateOf<Product?>(null) }
//    var quantityText by remember { mutableStateOf("1") }
//
//    Column {
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = !expanded }
//        ) {
//            OutlinedTextField(
//                value = selectedProduct?.name ?: "Selecione o Produto",
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor().fillMaxWidth()
//            )
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                products.forEach { product ->
//                    DropdownMenuItem(
//                        text = { Text("${product.name} (R$ ${product.price})") },
//                        onClick = {
//                            selectedProduct = product
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            OutlinedTextField(
//                value = quantityText,
//                onValueChange = { quantityText = it },
//                label = { Text("Qtd") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.weight(1f)
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(
//                onClick = {
//                    selectedProduct?.let { prod ->
//                        val qty = quantityText.toIntOrNull() ?: 1
//                        if (qty > 0) {
//                            onAddProduct(prod, qty)
//                            selectedProduct = null
//                            quantityText = "1"
//                        }
//                    }
//                },
//                enabled = selectedProduct != null
//            ) {
//                Icon(Icons.Default.Add, contentDescription = null)
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Add")
//            }
//        }
//    }
//}
//
//@Composable
//fun SaleItemRow(item: SaleItem, onRemove: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(text = item.productName, fontWeight = FontWeight.Bold)
//            Text(
//                text = "${item.quantity}x R$ ${item.unitPrice} = R$ ${item.subtotal}",
//                style = MaterialTheme.typography.bodySmall
//            )
//        }
//        IconButton(onClick = onRemove) {
//            Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Red)
//        }
//    }
//}
//
//@Composable
//fun SaleSuccessAlertDialog(onDismiss: () -> Unit) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
//        title = { Text(text = "Venda Realizada!") },
//        text = { Text("A venda foi salva com sucesso e sincronizada.") },
//        confirmButton = {
//            Button(onClick = onDismiss) {
//                Text("Nova Venda")
//            }
//        },
//        containerColor = MaterialTheme.colorScheme.surfaceVariant
//    )
//}

package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.model.SaleItem
import com.example.myapplication.ui.viewmodel.SaleViewModel

// --------------------------------------------------------
// UTILITÁRIO RÁPIDO PARA MÁSCARA (Apenas Visualização)
// --------------------------------------------------------
fun String.formatCpf(): String {
    // Remove tudo que não for dígito para garantir
    val digits = this.filter { it.isDigit() }
    return if (digits.length == 11) {
        "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6, 9)}-${digits.substring(9, 11)}"
    } else {
        this // Se não tiver 11 dígitos, retorna como está
    }
}

@Composable
fun SaleScreen(
    viewModel: SaleViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nova Venda",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SELEÇÃO DE CLIENTE ---
        Text("Cliente", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))

        ClientSelector(
            clients = state.availableClients,
            selectedClientName = state.currentSale.clientName,
            onClientSelected = { viewModel.selectClient(it) },
            onClientClear = { viewModel.clearClient() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- ADICIONAR PRODUTOS (Área de Ação) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Adicionar Item", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                ProductAdder(
                    products = state.availableProducts,
                    onAddProduct = { product, qty ->
                        viewModel.addProductToSale(product, qty)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- LISTA DE ITENS (CARRINHO) ---
        if (state.currentSale.items.isNotEmpty()) {
            Text("Itens da Venda", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(4.dp))

            state.currentSale.items.forEachIndexed { index, item ->
                SaleItemRow(
                    item = item,
                    onRemove = { viewModel.removeItem(index) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Totalizador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("TOTAL:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "R$ ${String.format("%.2f", state.currentSale.totalValue)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- DETALHES FINAIS ---
        // --- DETALHES FINAIS ---
        // CORREÇÃO: Lendo state.installmentCount em vez de state.currentSale.installments
        OutlinedTextField(
            value = state.installmentCount.toString(),
            onValueChange = {
                // Se o usuário apagar tudo ou digitar zero, assumimos 1 para evitar erros
                val number = it.toIntOrNull() ?: 0
                if (number >= 0) { // Impede números negativos enquanto digita
                    viewModel.updateInstallmentsCount(number)
                }
            },
            label = { Text("Parcelas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.currentSale.observation,
            onValueChange = { viewModel.updateObservation(it) },
            label = { Text("Observação") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÃO SALVAR ---
        Button(
            onClick = { viewModel.saveSale() },
            enabled = state.currentSale.items.isNotEmpty() &&
                    state.currentSale.clientId.isNotBlank() &&
                    !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Processando...")
            } else {
                Text("Finalizar Venda")
            }
        }

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }

        if (state.saveSuccess) {
            SaleSuccessAlertDialog(onDismiss = { viewModel.resetSuccessState() })
        }
    }
}

// --------------------------------------------------------
// COMPONENTES AUXILIARES
// --------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSelector(
    clients: List<Client>,
    selectedClientName: String,
    onClientSelected: (Client) -> Unit,
    onClientClear: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedClientName.ifBlank { "Selecione um cliente" },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = if (selectedClientName.isBlank()) Color.Gray else Color.Black,
                focusedTextColor = if (selectedClientName.isBlank()) Color.Gray else Color.Black
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // OPÇÃO REMOVER
            if (selectedClientName.isNotBlank()) {
                DropdownMenuItem(
                    text = { Text("Remover seleção", color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        onClientClear()
                        expanded = false
                    }
                )
                HorizontalDivider()
            }

            // LISTA
            clients.forEach { client ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(text = client.name, style = MaterialTheme.typography.bodyLarge)
                            // APLICAÇÃO DA MÁSCARA DE CPF AQUI
                            Text(
                                text = "CPF: ${client.cpf.formatCpf()}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    },
                    onClick = {
                        onClientSelected(client)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductAdder(
    products: List<Product>,
    onAddProduct: (Product, Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantityText by remember { mutableStateOf("1") }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedProduct?.name ?: "Selecione o Produto",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                // Deixa cinza se não tiver produto selecionado
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = if (selectedProduct == null) Color.Gray else Color.Black,
                    focusedTextColor = if (selectedProduct == null) Color.Gray else Color.Black
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // --- NOVO: OPÇÃO DE REMOVER PRODUTO SELECIONADO (Limpar o dropdown) ---
                if (selectedProduct != null) {
                    DropdownMenuItem(
                        text = { Text("Remover seleção", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            selectedProduct = null
                            quantityText = "1" // Reseta a quantidade tb se quiser
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                }

                products.forEach { product ->
                    DropdownMenuItem(
                        text = { Text("${product.name} (R$ ${product.price})") },
                        onClick = {
                            selectedProduct = product
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                label = { Text("Qtd") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    selectedProduct?.let { prod ->
                        val qty = quantityText.toIntOrNull() ?: 1
                        if (qty > 0) {
                            onAddProduct(prod, qty)
                            selectedProduct = null
                            quantityText = "1"
                        }
                    }
                },
                enabled = selectedProduct != null
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add")
            }
        }
    }
}

@Composable
fun SaleItemRow(item: SaleItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.productName, fontWeight = FontWeight.Bold)
            Text(
                text = "${item.quantity}x R$ ${item.unitPrice} = R$ ${item.subtotal}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Red)
        }
    }
}

@Composable
fun SaleSuccessAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
        title = { Text(text = "Venda Realizada!") },
        text = { Text("A venda foi salva com sucesso e sincronizada.") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Nova Venda")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

