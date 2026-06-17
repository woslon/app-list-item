package com.example.my_list_itens.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my_list_itens.ui.screen.ExportCsvScreen
import com.example.my_list_itens.ui.screen.HistoryScreen
import com.example.my_list_itens.ui.screen.ListScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {

        composable("list") {
            ListScreen(
                navController = navController
            )
        }

        composable("export_csv") {
            ExportCsvScreen(
                navController = navController
            )
        }

        composable("history") {
            HistoryScreen(
                navController = navController
            )
        }
    }
}