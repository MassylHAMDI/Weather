package com.example.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.ui.theme.WeaterTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

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
    val cities = listOf(
        City("London",icon = null, temperature = null, weather = null),
        City("Paris",icon = null, temperature = null, weather = null),
        City("New York",icon = null, temperature = null, weather = null)
    )
    Box(modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(id = backgroundImage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp)
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
                item {
                    AddCityCell(onClick = {})
                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WeaterTheme {
        HomeScreen()
    }
}