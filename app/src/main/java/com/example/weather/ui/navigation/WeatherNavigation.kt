package com.example.weather.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.ui.screens.home.HomeScreen
import com.example.weather.ui.screens.detail.components.DetailScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("detail/{cityName}") { backStackEntry ->
            DetailScreen(
                cityName = backStackEntry.arguments?.getString("cityName") ?: "",
                navController = navController
            )
        }
    }
}