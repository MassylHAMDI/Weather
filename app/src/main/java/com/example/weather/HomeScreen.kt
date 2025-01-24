package com.example.weather

import AddCityDialog
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
import androidx.compose.material3.ListItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.weater.R

import java.util.Calendar


@Composable
fun HomeScreen() {

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
                        WeatherCard(city)
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
        AddCityDialog(
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
fun HomeScreenPreview() {
    WeaterTheme {
        HomeScreen()
    }
}