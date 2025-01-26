package com.example.weather.service

import com.example.weather.api.WeatherApi
import com.example.weather.data.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {
    private val apiKey = "8908886f5b86f6e9ffae572768239f74"
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(WeatherApi::class.java)

    suspend fun getWeather(city: String): Result<WeatherResponse> = try {
        val response = api.getWeather(city, apiKey, "metric")
        println("📍 Weather data for $city: $response")
        Result.success(response)
    } catch (e: Exception) {
        println("❌ Error fetching weather for $city: ${e.message}")
        Result.failure(e)
    }
}
