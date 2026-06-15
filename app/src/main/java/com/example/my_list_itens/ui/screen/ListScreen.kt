package com.example.my_list_itens.ui.screen

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.my_list_itens.data.local.entity.Item
import com.example.my_list_itens.ui.viewmodel.ItemViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun listScreen(
    navController: NavController,
    viewModel: ItemViewModel = hiltViewModel()
) {

    var customer by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var editingItem by remember { mutableStateOf<Item?>(null) }
    var search by remember { mutableStateOf("") }
    var isDialog by remember { mutableStateOf(false) }

    val items by viewModel.getAll().collectAsState(initial = emptyList())

    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val totalGeral = items.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Itens") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF183729),
                    titleContentColor = Color.White
                ),
                actions = {

                    Surface(
                        onClick = {
                            navController.navigate("export_csv")
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.1f), // opacidade
                        tonalElevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Exportar",
                                color = Color.White
                            )

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "exportar",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialog = true },
                containerColor = Color(0xFF183729),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Pesquisar item") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            val filteredItems = items.filter {
                it.itemName.contains(search, ignoreCase = true)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2E7D32).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total da lista")
                    Text(
                        format.format(totalGeral),
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(filteredItems) { currentItem ->

                    val total = currentItem.price * currentItem.quantity

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(modifier = Modifier.weight(1f)) {

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, null)
                                        Spacer(Modifier.width(6.dp))
                                        Text("Cliente: ${currentItem.customer}")
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Inventory, null)
                                        Spacer(Modifier.width(6.dp))
                                        Text("Item: ${currentItem.itemName}")
                                    }
                                }

                                Row {

                                    IconButton(onClick = {
                                        editingItem = currentItem
                                        customer = currentItem.customer
                                        itemName = currentItem.itemName
                                        quantity = currentItem.quantity.toString()
                                        price = currentItem.price.toString()
                                        isDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, null)
                                    }

                                    IconButton(onClick = {
                                        viewModel.delete(currentItem)
                                    }) {
                                        Icon(Icons.Default.Delete, null, tint = Color.Red)
                                    }
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Quantidade"
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Quantidade: ${currentItem.quantity}")
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = "Preço"
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Preço: ${format.format(currentItem.price)}")
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFF2E7D32).copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Total: ${format.format(total)}",
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            if (isDialog) {
                Dialog(
                    onDismissRequest = { isDialog = false }
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Text(
                            text = "Adicionar item",
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            textAlign = TextAlign.Center
                        )

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            OutlinedTextField(customer, { customer = it }, label = { Text("Cliente") })
                            OutlinedTextField(itemName, { itemName = it }, label = { Text("Item") })
                            OutlinedTextField(quantity, { quantity = it }, label = { Text("Qtd") })
                            OutlinedTextField(price, { price = it }, label = { Text("Preço") })

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Button(
                                    onClick = { isDialog = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFC62828),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancelar")
                                }

                                Button(
                                    onClick = {

                                        if (editingItem == null) {
                                            viewModel.add(
                                                Item(
                                                    customer = customer,
                                                    itemName = itemName,
                                                    quantity = quantity.toIntOrNull() ?: 0,
                                                    price = price.replace(",", ".").toDoubleOrNull() ?: 0.0
                                                )
                                            )
                                        } else {
                                            viewModel.upsert(
                                                editingItem!!.copy(
                                                    customer = customer,
                                                    itemName = itemName,
                                                    quantity = quantity.toIntOrNull() ?: 0,
                                                    price = price.replace(",", ".").toDoubleOrNull() ?: 0.0
                                                )
                                            )
                                        }

                                        customer = ""
                                        itemName = ""
                                        quantity = ""
                                        price = ""
                                        editingItem = null
                                        isDialog = false

                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF183729),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(if (editingItem != null) "Editar" else "Salvar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}