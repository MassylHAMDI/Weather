package com.example.weather.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.example.weater.R
import com.example.weather.data.*
import com.example.weather.service.WeatherService
import com.example.weather.ui.screens.detail.AddCityDialog
import com.example.weather.ui.screens.home.components.WeatherCard
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val weatherService = remember { WeatherService() }
    var showAddDialog by remember { mutableStateOf(false) }
    val applicationContext = LocalContext.current
    val db = remember {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "weather_database").build()
    }
    val scope = rememberCoroutineScope()
    var cities by remember { mutableStateOf(emptyList<City>()) }

    // Animations
    val addButtonScale by animateFloatAsState(
        targetValue = if (showAddDialog) 0.8f else 1f,
        animationSpec = spring(dampingRatio = 0.7f)
    )

    // Background transition animation
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val isDaytime = currentHour in 6..18
    val backgroundImage = if (isDaytime) R.drawable.after_noon else R.drawable.night
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isDaytime) 0.7f else 0.5f,
        animationSpec = tween(1000)
    )

    // Time and date formatting
    val calendar = Calendar.getInstance()
    val timeText = android.text.format.DateFormat.format("HH:mm", calendar).toString()
    val dayText = android.text.format.DateFormat.format("EEEE, dd MMMM", calendar).toString().lowercase()

    // Initial data fetch
    LaunchedEffect(Unit) {
        scope.launch {
            cities = db.cityDao().getAll()
            cities.forEach { city ->
                weatherService.getWeather(city.name)
                    .onSuccess { response ->
                        val updatedCity = city.copy(
                            temperature = response.main.temp,
                            weather = response.weather.firstOrNull()?.main,
                            icon = response.weather.firstOrNull()?.icon
                        )
                        db.cityDao().update(updatedCity)
                    }
            }
            cities = db.cityDao().getAll()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated background
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 3.dp),
            contentScale = ContentScale.Crop,
            alpha = backgroundAlpha
        )

        // Gradient overlay with animation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6B8DD6).copy(alpha = 0.4f),
                            Color(0xFF8F6BD6).copy(alpha = 0.6f)
                        )
                    )
                )
        )

        Column {
            // Modern TopAppBar with glass effect
            TopAppBar(
                title = {
                    Text(
                        text = "Weather",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                offset = Offset(1f, 1f),
                                blurRadius = 4f
                            )
                        ),
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .scale(addButtonScale)
                            .padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add city",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    )
            )

            // Animated time and date display
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timeText,
                    fontSize = 72.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 8f
                        )
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dayText,
                    fontSize = 20.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.2f),
                            offset = Offset(1f, 1f),
                            blurRadius = 4f
                        )
                    )
                )
            }

            // Cities list with staggered animation
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = cities,
                    key = { city -> city.name }
                ) { city ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(
                            initialAlpha = 0.3f
                        ) + slideInVertically(
                            initialOffsetY = { it / 2 }
                        ) + expandVertically(
                            expandFrom = Alignment.Top
                        ),
                        exit = fadeOut() + slideOutVertically() + shrinkVertically()
                    ) {
                        WeatherCard(
                            city = city,
                            onCityClick = {
                                navController.navigate("detail/${city.name}")
                            }
                        )
                    }
                }
            }
        }
    }

    // Add city dialog with animation
    AnimatedVisibility(
        visible = showAddDialog,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        AddCityDialog(
            onDismiss = { showAddDialog = false },
            onAddCity = { name ->
                scope.launch {
                    val newCity = City(name = name)
                    db.cityDao().insert(newCity)
                    weatherService.getWeather(name)
                        .onSuccess { response ->
                            val updatedCity = newCity.copy(
                                temperature = response.main.temp,
                                weather = response.weather.firstOrNull()?.main,
                                icon = response.weather.firstOrNull()?.icon
                            )
                            db.cityDao().update(updatedCity)
                        }
                    cities = db.cityDao().getAll()
                }
                showAddDialog = false
            }
        )
    }
}










/*import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.example.weather.ui.theme.WeaterTheme
import java.util.Calendar
import com.example.weater.R
import com.example.weather.data.AppDatabase

import com.example.weather.ui.screens.detail.AddCityDialog
import com.example.weather.ui.screens.home.components.WeatherCard
import com.example.weather.data.City
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }
    *//*var cities by remember { mutableStateOf(listOf(
        City("London"),
        City("Paris"),
        City("New York")
    )) }*//*


    val applicationContext = LocalContext.current
    val db = remember { Room.databaseBuilder(applicationContext, AppDatabase::class.java, "weather_database").build()}
    val scope = rememberCoroutineScope()
    var cities by remember { mutableStateOf(emptyList<City>()) }
    LaunchedEffect(Unit) {
        scope.launch {
            cities = db.cityDao().getAll()
        }
    }

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val backgroundImage = if (currentHour in 6..18) R.drawable.after_noon else R.drawable.night

    val calendar = Calendar.getInstance()
    val timeText = android.text.format.DateFormat.format("HH:mm", calendar).toString()
    val dayText = android.text.format.DateFormat.format("dd MMMM", calendar).toString().lowercase()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add city",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timeText,
                    fontSize = 70.sp,
                    color = Color.White
                )
                Text(
                    text = dayText,
                    fontSize = 20.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            LazyColumn(modifier = Modifier.height(300.dp)) {
                cities.forEach { city ->
                    item {
                        WeatherCard(
                            city = city,
                            onCityClick = {
                                navController.navigate("detail/${city.name}")
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCityDialog(
            onDismiss = { showAddDialog = false },
            onAddCity = { name ->
                scope.launch {
                    val newCity = City(name = name)
                    db.cityDao().insert(newCity)
                    cities = db.cityDao().getAll()
                }
                showAddDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WeaterTheme {
        HomeScreen( navController = NavController(LocalContext.current))
    }
}*/

/*
package com.example.weather

import com.example.weather.AddCityDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.ui.theme.WeaterTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weater.R

import java.util.Calendar


@Composable
fun com.example.weather.HomeScreen(navController: NavController) {

    // Get the current hour
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val backgroundImage = if (currentHour in 6..18) R.drawable.after_noon else R.drawable.night

    // Get the current date and time
    val calendar = Calendar.getInstance()
    val timeText = android.text.format.DateFormat.format("HH:mm", calendar).toString()
    val dayText = android.text.format.DateFormat.format("dd MMMM", calendar).toString().lowercase()

    //
    var cities by remember { mutableStateOf(listOf(
        City("London", null, null, null),
        City("Paris", null, null, null),
        City("New York", null, null, null)
    )) }
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timeText,
                    fontSize = 70.sp,
                    color = Color.White
                )
                Text(
                    text = dayText,
                    fontSize = 20.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            LazyColumn(modifier = Modifier.height(450.dp)) {
                cities.forEach { city ->
                    item {
                        WeatherCard(
                            city = city,
                            onCityClick = {
                                navController.navigate("detail/${city.name}")
                            })
                    }

                }

            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 80.dp, vertical = 3.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showAddDialog = true },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        "Add City",
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.White
                    )
                }
            }


        }
    }
    if (showAddDialog) {
        com.example.weather.AddCityDialog(
            onDismiss = { showAddDialog = false },
            onAddCity = { name ->
                cities = cities + City(name, null, null, null)  // Remove .value
                showAddDialog = false
            }
        )
    }

}



@Preview(showBackground = true)
@Composable
fun com.example.weather.HomeScreenPreview() {
    WeaterTheme {
        com.example.weather.HomeScreen( navController = NavController(LocalContext.current))
    }
}*/
