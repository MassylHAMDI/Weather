package com.example.weather

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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