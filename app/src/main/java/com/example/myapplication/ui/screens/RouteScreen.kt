//package com.example.myapplication.ui.screens
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.myapplication.domain.model.Client
//import com.example.myapplication.domain.model.Installment
//import com.example.myapplication.ui.viewmodel.RouteViewModel
//// --- IMPORTANTE: Importando as funções do arquivo Extensions.kt ---
//// Se der erro de importação, verifique se o arquivo ui/utils/Extensions.kt existe
//import com.example.myapplication.ui.util.formatCpf
//import com.example.myapplication.ui.util.formatDate
//
//@Composable
//fun RouteScreen(
//    viewModel: RouteViewModel = hiltViewModel()
//) {
//    val state by viewModel.uiState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF2F2F7))
//            .padding(16.dp)
//    ) {
//        // --- CABEÇALHO ---
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = if (state.isReorderMode) "Organizando..." else "Rota de Cobrança",
//                style = MaterialTheme.typography.headlineSmall.copy(
//                    fontWeight = FontWeight.Bold,
//                    letterSpacing = (-0.5).sp
//                ),
//                color = Color.Black
//            )
//
//            IconButton(
//                onClick = { viewModel.toggleReorderMode() },
//                modifier = Modifier.background(if(state.isReorderMode) MaterialTheme.colorScheme.primary else Color.White, RoundedCornerShape(10.dp))
//            ) {
//                Icon(
//                    imageVector = if (state.isReorderMode) Icons.Default.Check else Icons.Default.Edit,
//                    contentDescription = "Organizar Rota",
//                    tint = if(state.isReorderMode) Color.White else MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- FILTRO DE CIDADE ---
//        AnimatedVisibility(visible = !state.isReorderMode) {
//            CitySelector(
//                cities = state.availableCities,
//                selectedCity = state.selectedCity,
//                onCitySelected = { viewModel.selectCity(it) }
//            )
//        }
//
//        Spacer(modifier = Modifier.height(if (!state.isReorderMode) 16.dp else 0.dp))
//
//        // --- LISTA DE CLIENTES ---
//        if (state.clientsInRoute.isEmpty()) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                val msg = if (state.selectedCity.isBlank()) "Selecione uma cidade acima." else "Nenhum cliente encontrado."
//                Text(msg, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
//            }
//        } else {
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.weight(1f)
//            ) {
//                itemsIndexed(state.clientsInRoute, key = { _, client -> client.id }) { index, client ->
//                    val clientInstallments = state.clientDebts[client.id] ?: emptyList()
//
//                    RouteClientCard(
//                        client = client,
//                        positionIndex = index + 1,
//                        isFirst = index == 0,
//                        isLast = index == state.clientsInRoute.lastIndex,
//                        allInstallments = clientInstallments,
//                        isExpanded = state.expandedClientId == client.id,
//                        isReorderMode = state.isReorderMode,
//                        onExpandClick = { viewModel.toggleClientExpanded(client.id) },
//                        onPaymentSubmit = { installment, value ->
//                            viewModel.registerPayment(installment, value)
//                        },
//                        onReferenceUpdate = { newRef ->
//                            viewModel.updateReferencePoint(client.id, newRef)
//                        },
//                        onMoveUp = { viewModel.moveClient(index, index - 1) },
//                        onMoveDown = { viewModel.moveClient(index, index + 1) }
//                    )
//                }
//                item { Spacer(modifier = Modifier.height(80.dp)) }
//            }
//        }
//
//        if (state.paymentSuccess) {
//            SuccessFeedback(onDismiss = { viewModel.resetPaymentSuccess() })
//        }
//    }
//}
//
//// --------------------------------------------------------
//// COMPONENTES VISUAIS DA ROTA
//// --------------------------------------------------------
//
//@Composable
//fun RouteClientCard(
//    client: Client,
//    positionIndex: Int,
//    isFirst: Boolean,
//    isLast: Boolean,
//    allInstallments: List<Installment>,
//    isExpanded: Boolean,
//    isReorderMode: Boolean,
//    onExpandClick: () -> Unit,
//    onPaymentSubmit: (Installment, Double) -> Unit,
//    onReferenceUpdate: (String) -> Unit,
//    onMoveUp: () -> Unit,
//    onMoveDown: () -> Unit
//) {
//    val totalDebt = remember(allInstallments) {
//        allInstallments.filter { !it.isPaid }.sumOf { it.remainingAmount }
//    }
//
//    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rot")
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { if (!isReorderMode) onExpandClick() },
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        border = if (isExpanded) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)) else null
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//
//            // 1. CABEÇALHO DO CLIENTE
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // NÚMERO (Squircle)
//                Surface(
//                    modifier = Modifier.size(40.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    color = if (isReorderMode) Color.LightGray.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primaryContainer,
//                ) {
//                    Box(contentAlignment = Alignment.Center) {
//                        Text(
//                            text = "$positionIndex",
//                            color = if (isReorderMode) Color.Gray else MaterialTheme.colorScheme.primary,
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.ExtraBold,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                // NOME E CPF
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = client.name,
//                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
//                        color = Color.Black
//                    )
//
//                    val subtitle = if (client.cpf.isNotBlank()) {
//                        "CPF: ${client.cpf.formatCpf()}"
//                    } else {
//                        client.phone.ifBlank { "Sem detalhes" }
//                    }
//
//                    Text(
//                        text = subtitle,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.Gray,
//                        maxLines = 1
//                    )
//                }
//
//                // AÇÃO
//                if (isReorderMode) {
//                    Row {
//                        FilledIconButton(
//                            onClick = onMoveUp,
//                            enabled = !isFirst,
//                            modifier = Modifier.size(36.dp),
//                            colors = IconButtonDefaults.filledIconButtonColors(
//                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                                contentColor = MaterialTheme.colorScheme.primary,
//                                disabledContainerColor = Color.Transparent,
//                                disabledContentColor = Color.LightGray
//                            )
//                        ) {
//                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Subir")
//                        }
//
//                        Spacer(modifier = Modifier.width(4.dp))
//
//                        FilledIconButton(
//                            onClick = onMoveDown,
//                            enabled = !isLast,
//                            modifier = Modifier.size(36.dp),
//                            colors = IconButtonDefaults.filledIconButtonColors(
//                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                                contentColor = MaterialTheme.colorScheme.primary,
//                                disabledContainerColor = Color.Transparent,
//                                disabledContentColor = Color.LightGray
//                            )
//                        ) {
//                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descer")
//                        }
//                    }
//                } else {
//                    Column(horizontalAlignment = Alignment.End) {
//                        Text(
//                            text = "R$ ${String.format("%.2f", totalDebt)}",
//                            fontWeight = FontWeight.Bold,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = if (totalDebt > 0) MaterialTheme.colorScheme.error else Color(0xFF34C759)
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowDown,
//                            contentDescription = null,
//                            modifier = Modifier.rotate(rotationState).size(20.dp),
//                            tint = Color.Gray
//                        )
//                    }
//                }
//            }
//
//            // 2. CONTEÚDO EXPANDIDO
//            AnimatedVisibility(visible = isExpanded && !isReorderMode) {
//                Column(modifier = Modifier.padding(top = 20.dp)) {
//                    Divider(color = Color(0xFFE5E5EA), thickness = 1.dp)
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    AddressSection(client, onReferenceUpdate)
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Text(
//                        "Histórico de Compras",
//                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    if (allInstallments.isEmpty()) {
//                        Text("Nenhuma compra registrada.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
//                    } else {
//                        val salesGrouped = remember(allInstallments) {
//                            allInstallments.groupBy { it.saleId }
//                        }
//
//                        salesGrouped.forEach { (saleId, installments) ->
//                            val isSalePaid = installments.all { it.isPaid }
//                            if (!isSalePaid) {
//                                ExpandableSaleRow(
//                                    saleId = saleId,
//                                    installments = installments,
//                                    onPaymentSubmit = onPaymentSubmit
//                                )
//                                Spacer(modifier = Modifier.height(12.dp))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AddressSection(
//    client: Client,
//    onReferenceUpdate: (String) -> Unit
//) {
//    var showDialog by remember { mutableStateOf(false) }
//    val currentRef = client.address.userComplements["user_padrao"] ?: ""
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFF2F2F7), RoundedCornerShape(12.dp))
//            .padding(12.dp)
//    ) {
//        Row(verticalAlignment = Alignment.Top) {
//            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                AddressLabelRow("Rua", "${client.address.street}, Nº ${client.address.number}")
//                AddressLabelRow("Bairro", client.address.neighborhood)
//                AddressLabelRow("Cidade", client.address.city)
//
//                if (currentRef.isNotBlank()) {
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        text = "Ref: $currentRef",
//                        style = MaterialTheme.typography.bodySmall,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.secondary
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        TextButton(
//            onClick = { showDialog = true },
//            modifier = Modifier.align(Alignment.End).height(32.dp),
//            contentPadding = PaddingValues(horizontal = 8.dp)
//        ) {
//            Text("Editar Referência", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
//        }
//    }
//
//    if (showDialog) {
//        ReferenceDialog(
//            initialValue = currentRef,
//            onDismiss = { showDialog = false },
//            onConfirm = { newRef ->
//                onReferenceUpdate(newRef)
//                showDialog = false
//            }
//        )
//    }
//}
//
//@Composable
//fun AddressLabelRow(label: String, value: String) {
//    Row(modifier = Modifier.padding(bottom = 2.dp)) {
//        Text(
//            text = "$label: ",
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Gray,
//            fontWeight = FontWeight.Medium
//        )
//        Text(
//            text = value,
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Black,
//            fontWeight = FontWeight.SemiBold
//        )
//    }
//}
//
//@Composable
//fun ExpandableSaleRow(
//    saleId: String,
//    installments: List<Installment>,
//    onPaymentSubmit: (Installment, Double) -> Unit
//) {
//    var isExpanded by remember { mutableStateOf(false) }
//    val totalRemaining = installments.filter { !it.isPaid }.sumOf { it.remainingAmount }
//    val firstDate = installments.minOfOrNull { it.dueDate } ?: 0L
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(10.dp))
//            .background(Color.White)
//            .border(1.dp, Color(0xFFE5E5EA), RoundedCornerShape(10.dp))
//    ) {
//        Row(
//            modifier = Modifier
//                .clickable { isExpanded = !isExpanded }
//                .padding(12.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text("Compra de ${firstDate.formatDate()}", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
//                Text("${installments.size} parcelas", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
//            }
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    "R$ ${String.format("%.2f", totalRemaining)}",
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.error,
//                    fontSize = 14.sp
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Icon(
//                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                    contentDescription = null,
//                    tint = Color.Gray,
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//        }
//
//        AnimatedVisibility(visible = isExpanded) {
//            Column {
//                Divider(color = Color(0xFFE5E5EA))
//                installments.sortedBy { it.number }.forEach { installment ->
//                    if (!installment.isPaid) {
//                        PaymentRow(installment, onPaymentSubmit)
//                        Divider(color = Color(0xFFF2F2F7), thickness = 1.dp)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PaymentRow(
//    installment: Installment,
//    onPay: (Installment, Double) -> Unit
//) {
//    var amountText by remember { mutableStateOf("") }
//
//    val isLate = installment.dueDate < System.currentTimeMillis()
//    val isPartial = installment.amountPaid > 0
//
//    val statusText = when {
//        isLate -> "VENCEU"
//        isPartial -> "PARCIAL"
//        else -> "ABERTO"
//    }
//
//    val statusColor = when {
//        isLate -> Color(0xFFFF3B30)
//        isPartial -> Color(0xFFFF9500)
//        else -> Color(0xFF8E8E93)
//    }
//
//    Column(modifier = Modifier.padding(12.dp)) {
//        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(8.dp)
//                        .background(statusColor, CircleShape)
//                )
//                Spacer(modifier = Modifier.width(6.dp))
//                Text("${installment.number}ª Parcela", fontWeight = FontWeight.Medium, fontSize = 14.sp)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(statusText, color = statusColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
//            }
//            Text(installment.dueDate.formatDate(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
//        }
//
//        Spacer(modifier = Modifier.height(6.dp))
//
//        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//            Text(
//                text = "Falta: R$ ${String.format("%.2f", installment.remainingAmount)}",
//                fontWeight = FontWeight.ExtraBold,
//                fontSize = 15.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            BasicTextField(
//                value = amountText,
//                onValueChange = { amountText = it },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                singleLine = true,
//                decorationBox = { innerTextField ->
//                    Box(
//                        modifier = Modifier
//                            .width(70.dp)
//                            .height(32.dp)
//                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
//                            .padding(horizontal = 8.dp, vertical = 6.dp),
//                        contentAlignment = Alignment.CenterStart
//                    ) {
//                        if (amountText.isEmpty()) Text("Valor", color = Color.LightGray, fontSize = 12.sp)
//                        innerTextField()
//                    }
//                }
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(
//                onClick = {
//                    val value = amountText.toDoubleOrNull()
//                    if (value != null && value > 0) {
//                        onPay(installment, value)
//                        amountText = ""
//                    }
//                },
//                enabled = amountText.isNotBlank(),
//                shape = RoundedCornerShape(8.dp),
//                contentPadding = PaddingValues(horizontal = 20.dp),
//                modifier = Modifier.height(44.dp)
//            ) {
//                Text("Baixar")
//            }
//        }
//    }
//}
//
//@Composable
//fun ReferenceDialog(
//    initialValue: String,
//    onDismiss: () -> Unit,
//    onConfirm: (String) -> Unit
//) {
//    var text by remember { mutableStateOf(initialValue) }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        containerColor = Color.White,
//        title = { Text("Ponto de Referência") },
//        text = {
//            OutlinedTextField(
//                value = text,
//                onValueChange = { text = it },
//                label = { Text("Ex: Casa verde, portão branco") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = MaterialTheme.colorScheme.primary,
//                    unfocusedBorderColor = Color.LightGray
//                )
//            )
//        },
//        confirmButton = {
//            Button(onClick = { onConfirm(text) }) {
//                Text("Salvar")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancelar")
//            }
//        }
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CitySelector(
//    cities: List<String>,
//    selectedCity: String,
//    onCitySelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        OutlinedTextField(
//            value = selectedCity.ifBlank { "Selecione a Cidade" },
//            onValueChange = {},
//            readOnly = true,
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            modifier = Modifier.menuAnchor().fillMaxWidth(),
//            shape = RoundedCornerShape(8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedContainerColor = Color.White,
//                focusedContainerColor = Color.White,
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = MaterialTheme.colorScheme.primary
//            ),
//            placeholder = { Text("Filtrar por cidade") }
//        )
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.background(Color.White)
//        ) {
//            cities.forEach { city ->
//                DropdownMenuItem(
//                    text = { Text(city) },
//                    onClick = {
//                        onCitySelected(city)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun SuccessFeedback(onDismiss: () -> Unit) {
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF34C759)),
//        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
//            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
//            Spacer(modifier = Modifier.width(12.dp))
//            Text("Pagamento registrado com sucesso!", color = Color.White, fontWeight = FontWeight.Bold)
//        }
//    }
//    LaunchedEffect(Unit) {
//        kotlinx.coroutines.delay(2500)
//        onDismiss()
//    }
//}

package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.model.Installment
import com.example.myapplication.domain.model.Sale
import com.example.myapplication.ui.util.formatCpf
import com.example.myapplication.ui.util.formatDate
import com.example.myapplication.ui.util.formatFullDate
import com.example.myapplication.ui.viewmodel.RouteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    viewModel: RouteViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Estado para controlar o Modal de baixo para cima
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // --- MODAL DE DETALHES DA VENDA (NOVA JANELA) ---
    if (state.selectedSale != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeSaleDetails() },
            sheetState = sheetState,
            containerColor = Color(0xFFF2F2F7),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            SaleDetailsContent(
                sale = state.selectedSale!!,
                onPaymentSubmit = { inst, value -> viewModel.registerPayment(inst, value) }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(16.dp)
    ) {
        // --- CABEÇALHO ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (state.isReorderMode) "Organizando Rota..." else "Rota de Cobrança",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = Color.Black
            )

            IconButton(
                onClick = { viewModel.toggleReorderMode() },
                modifier = Modifier.background(
                    if(state.isReorderMode) MaterialTheme.colorScheme.primary else Color.White,
                    RoundedCornerShape(10.dp)
                )
            ) {
                Icon(
                    imageVector = if (state.isReorderMode) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = "Organizar Rota",
                    tint = if(state.isReorderMode) Color.White else MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FILTRO DE CIDADE ---
        AnimatedVisibility(visible = !state.isReorderMode) {
            CitySelector(
                cities = state.availableCities,
                selectedCity = state.selectedCity,
                onCitySelected = { viewModel.selectCity(it) }
            )
        }

        Spacer(modifier = Modifier.height(if (!state.isReorderMode) 16.dp else 0.dp))

        // --- LISTA DE CLIENTES ---
        if (state.clientsInRoute.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                val msg = if (state.selectedCity.isBlank()) "Selecione uma cidade acima." else "Nenhum cliente encontrado."
                Text(msg, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(state.clientsInRoute, key = { _, client -> client.id }) { index, client ->
                    val clientInstallments = state.clientDebts[client.id] ?: emptyList()

                    RouteClientCard(
                        client = client,
                        positionIndex = index + 1,
                        isFirst = index == 0,
                        isLast = index == state.clientsInRoute.lastIndex,
                        allInstallments = clientInstallments,
                        isExpanded = state.expandedClientId == client.id,
                        isReorderMode = state.isReorderMode,
                        onExpandClick = { viewModel.toggleClientExpanded(client.id) },
                        onReferenceUpdate = { newRef ->
                            viewModel.updateReferencePoint(client.id, newRef)
                        },
                        onMoveUp = { viewModel.moveClient(index, index - 1) },
                        onMoveDown = { viewModel.moveClient(index, index + 1) },
                        // Callback novo para abrir o modal
                        onSaleClick = { saleId -> viewModel.openSaleDetails(saleId) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        if (state.paymentSuccess) {
            SuccessFeedback(onDismiss = { viewModel.resetPaymentSuccess() })
        }
    }
}

// --------------------------------------------------------
// CARD DO CLIENTE
// --------------------------------------------------------

@Composable
fun RouteClientCard(
    client: Client,
    positionIndex: Int,
    isFirst: Boolean,
    isLast: Boolean,
    allInstallments: List<Installment>,
    isExpanded: Boolean,
    isReorderMode: Boolean,
    onExpandClick: () -> Unit,
    onReferenceUpdate: (String) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onSaleClick: (String) -> Unit
) {
    val totalDebt = remember(allInstallments) {
        allInstallments.filter { !it.isPaid }.sumOf { it.remainingAmount }
    }

    // Agrupa parcelas por ID da Venda para mostrar histórico resumido
    val salesGroups = remember(allInstallments) {
        allInstallments.groupBy { it.saleId }
    }

    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rot")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!isReorderMode) onExpandClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (isExpanded) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 1. CABEÇALHO DO CLIENTE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // NÚMERO (Squircle)
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isReorderMode) Color.LightGray.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$positionIndex",
                            color = if (isReorderMode) Color.Gray else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // NOME E CPF
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = client.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )

                    val subtitle = if (client.cpf.isNotBlank()) {
                        "CPF: ${client.cpf.formatCpf()}"
                    } else {
                        client.phone.ifBlank { "Toque para ver detalhes" }
                    }

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }

                // AÇÃO (Valor ou Botões de Mover)
                if (isReorderMode) {
                    Row {
                        FilledIconButton(
                            onClick = onMoveUp,
                            enabled = !isFirst,
                            modifier = Modifier.size(36.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.LightGray
                            )
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Subir")
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        FilledIconButton(
                            onClick = onMoveDown,
                            enabled = !isLast,
                            modifier = Modifier.size(36.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.LightGray
                            )
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descer")
                        }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "R$ ${String.format("%.2f", totalDebt)}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (totalDebt > 0) MaterialTheme.colorScheme.error else Color(0xFF34C759)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationState).size(20.dp),
                            tint = Color.Gray
                        )
                    }
                }
            }

            // 2. CONTEÚDO EXPANDIDO
            AnimatedVisibility(visible = isExpanded && !isReorderMode) {
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Divider(color = Color(0xFFE5E5EA), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Endereço e Referência
                    AddressSection(client, onReferenceUpdate)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Histórico de Compras",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (salesGroups.isEmpty()) {
                        Text("Nenhuma compra registrada.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    } else {
                        // LISTA DE "COMPRAS" (CARRINHOS)
                        salesGroups.forEach { (saleId, installments) ->
                            val isPaid = installments.all { it.isPaid }
                            val date = installments.firstOrNull()?.dueDate ?: 0L // Usa data da 1a parcela como ref visual
                            val totalSaleDebt = installments.filter { !it.isPaid }.sumOf { it.remainingAmount }

                            PurchaseSummaryRow(
                                date = date,
                                totalPending = totalSaleDebt,
                                isFullyPaid = isPaid,
                                onClick = { onSaleClick(saleId) }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------
// COMPONENTES DA LISTA PRINCIPAL
// --------------------------------------------------------

@Composable
fun PurchaseSummaryRow(
    date: Long,
    totalPending: Double,
    isFullyPaid: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FA)) // Cinza bem claro para destacar
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone de sacola
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(if(isFullyPaid) Color(0xFFE8F5E9) else Color(0xFFFFF3E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if(isFullyPaid) Icons.Outlined.ReceiptLong else Icons.Outlined.ShoppingBag,
                contentDescription = null,
                tint = if(isFullyPaid) Color(0xFF2E7D32) else Color(0xFFFF9800),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Compra de ${date.formatDate()}",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = if (isFullyPaid) "Quitada" else "Em aberto",
                style = MaterialTheme.typography.bodySmall,
                color = if (isFullyPaid) Color(0xFF2E7D32) else Color.Gray
            )
        }

        if (!isFullyPaid) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Faltam R$ ${String.format("%.2f", totalPending)}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun AddressSection(
    client: Client,
    onReferenceUpdate: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val currentRef = client.address.userComplements["user_padrao"] ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F7), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                AddressLabelRow("Rua", "${client.address.street}, Nº ${client.address.number}")
                AddressLabelRow("Bairro", client.address.neighborhood)
                AddressLabelRow("Cidade", client.address.city)

                if (currentRef.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ref: $currentRef",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.End).height(32.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text("Editar Referência", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }

    if (showDialog) {
        ReferenceDialog(
            initialValue = currentRef,
            onDismiss = { showDialog = false },
            onConfirm = { newRef ->
                onReferenceUpdate(newRef)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddressLabelRow(label: String, value: String) {
    Row(modifier = Modifier.padding(bottom = 2.dp)) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// --------------------------------------------------------
// CONTEÚDO DO MODAL (JANELA DE DETALHES)
// --------------------------------------------------------

@Composable
fun SaleDetailsContent(
    sale: Sale,
    onPaymentSubmit: (Installment, Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. CABEÇALHO DA JANELA ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.ReceiptLong, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Detalhes da Compra", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(sale.date.formatFullDate(), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Vendedor: user_padrao", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
            }
        }

        // --- 2. O CARRINHO (ITENS) ---
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Carrinho de Compras", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF2F2F7))

                if (sale.items.isEmpty()) {
                    Text("Detalhes dos itens não disponíveis.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                } else {
                    sale.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${item.quantity}x  ${item.productName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "R$ ${String.format("%.2f", item.subtotal)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF2F2F7))

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Total da Compra", fontWeight = FontWeight.Bold)
                        Text("R$ ${String.format("%.2f", sale.totalValue)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. PARCELAS INTELIGENTES (VISUAL TIMELINE) ---
        Text(
            "Plano de Pagamento",
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val sortedInstallments = sale.installments.sortedBy { it.number }
                sortedInstallments.forEachIndexed { index, installment ->
                    SmartInstallmentRow(
                        installment = installment,
                        isLast = index == sortedInstallments.lastIndex,
                        onPay = onPaymentSubmit
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SmartInstallmentRow(
    installment: Installment,
    isLast: Boolean,
    onPay: (Installment, Double) -> Unit
) {
    val isPaid = installment.isPaid
    val isLate = !isPaid && installment.dueDate < System.currentTimeMillis()

    val statusColor = when {
        isPaid -> Color(0xFF34C759) // Verde
        isLate -> Color(0xFFFF3B30) // Vermelho
        else -> MaterialTheme.colorScheme.primary // Azul padrão
    }

    // Layout Intrinsecamente alto para a linha do tempo funcionar
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

        // COLUNA DA LINHA DO TEMPO
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            // Bolinha
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(if (isPaid) statusColor else Color.White, CircleShape)
                    .border(2.dp, statusColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if(isPaid) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                }
            }
            // Linha conectora
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight() // Preenche até o próximo item
                        .background(Color(0xFFE5E5EA))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // CONTEÚDO DA PARCELA
        Column(modifier = Modifier.weight(1f).padding(bottom = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Parcela ${installment.number}",
                    fontWeight = FontWeight.Bold,
                    color = if(isPaid) Color.Gray else Color.Black
                )
                Text(
                    installment.dueDate.formatDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = if(isLate) statusColor else Color.Gray,
                    fontWeight = if(isLate) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "R$ ${String.format("%.2f", installment.originalValue)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textDecoration = if(isPaid) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )

                if (!isPaid && installment.amountPaid > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "(Pago: R$ ${String.format("%.2f", installment.amountPaid)})",
                        fontSize = 12.sp,
                        color = Color(0xFFFF9500),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // CAMPO DE PAGAMENTO (Aparece apenas se não estiver 100% pago)
            if (!isPaid) {
                Spacer(modifier = Modifier.height(12.dp))
                SmartPaymentInput(installment, onPay)
            }
        }
    }
}

@Composable
fun SmartPaymentInput(
    installment: Installment,
    onPay: (Installment, Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFFF2F2F7), RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        BasicTextField(
            value = amountText,
            onValueChange = { amountText = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier
                .width(100.dp)
                .padding(start = 8.dp, end = 8.dp),
            decorationBox = { inner ->
                if (amountText.isEmpty()) Text("Digite o valor", fontSize = 14.sp, color = Color.Gray)
                inner()
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val value = amountText.toDoubleOrNull()
                if (value != null && value > 0) {
                    onPay(installment, value)
                    amountText = ""
                }
            },
            enabled = amountText.isNotEmpty(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(36.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("Baixar", fontSize = 14.sp)
        }
    }
}

// --------------------------------------------------------
// COMPONENTES UTILITÁRIOS (DIÁLOGOS, DROPDOWN, FEEDBACK)
// --------------------------------------------------------

@Composable
fun ReferenceDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Ponto de Referência") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Ex: Casa verde, portão branco") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(text) }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelector(
    cities: List<String>,
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCity.ifBlank { "Selecione a Cidade" },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            placeholder = { Text("Filtrar por cidade") }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SuccessFeedback(onDismiss: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF34C759)),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Pagamento registrado com sucesso!", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2500)
        onDismiss()
    }
}

// Função Helper para controlar tamanho na linha do tempo
fun Modifier.intrinsicMinHeight(min: Dp) = this.heightIn(min = min)