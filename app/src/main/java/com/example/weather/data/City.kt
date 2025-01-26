package com.example.weather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String? = null,
    val temperature: Double? = null,
    val weather: String? = null


)