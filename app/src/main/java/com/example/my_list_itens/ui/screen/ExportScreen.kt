package com.example.my_list_itens.ui.screen

import android.R
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.my_list_itens.data.local.entity.History
import com.example.my_list_itens.ui.viewmodel.ItemViewModel
import com.example.my_list_itens.ui.viewmodel.HistoryViewModel
import com.example.my_list_itens.utils.CsvUtils
import com.example.my_list_itens.utils.Alert
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    navController: NavController,

) {
    val itemViewModel: ItemViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()

    var fileName by remember {
        mutableStateOf("lista_itens")
    }

    var includeHeader by remember {
        mutableStateOf(true)
    }

    var exportedFile by remember {
        mutableStateOf<Uri?>(null)
    }

    val items by itemViewModel.getAll()
        .collectAsState(initial = emptyList())

    val format = NumberFormat.getCurrencyInstance(
        Locale("pt", "BR")
    )

    val total = items.sumOf {
        it.price * it.quantity
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Exportar CSV",
                        color = Color.White
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = { navController.navigate("history") }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                            contentDescription = "Mais opções",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF183729),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card( modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "RESUMO DA EXPORTAÇÃO",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Itens: ${items.size}"
                    )

                    Text(
                        text = "Total: ${format.format(total)}"
                    )
                }
            }

            OutlinedTextField(
                value = fileName,
                onValueChange = {
                    fileName = it
                },
                label = {
                    Text("Nome do arquivo")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = includeHeader,
                        onCheckedChange = {
                            includeHeader = it
                        }
                    )

                    Text(
                        text = "Incluir cabeçalho CSV"
                    )
                }
            }

            Button(
                onClick = {

                    context.Alert(
                        msg = "Gerando arquivo CSV.",
                        title = " Aguarde! ",
                    )

                    val csv = CsvUtils.generateCsv(
                        items = items,
                        includeHeader = includeHeader
                    )


                    val uri = CsvUtils.saveCsv(
                        context = context,
                        fileName = fileName,
                        content = csv
                    )

                    exportedFile =  uri

                    historyViewModel.add(
                        History(
                            fileName = "$fileName.csv",
                            fileUri = uri.toString(),
                            date = LocalDate.now().toString(),
                            totalItens = items.size
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF183729)
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    modifier = Modifier,
                    color = Color.White,
                    text = "Gerar CSV"
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Última exportação",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "${fileName}.csv"
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    if( exportedFile === null ){
                        context.Alert(
                            msg = "Gere o arquivo antes de compartilhar.",
                            title = "Ops !",

                            )
                    }

                    exportedFile?.let {uri ->
                        CsvUtils.shareCsv(
                            context = context, uri = uri
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text("Compartilhar Arquivo")
            }
        }
    }
}