package com.example.weather.data

import androidx.room.*

@Dao
interface CityDao {

    @Insert
    suspend fun insert(city: City)

    @Query("SELECT * FROM cities WHERE name = :name")
    suspend fun getCityByName(name: String): City?

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCityById(id: Int): City?

    @Query("SELECT * FROM cities")
    suspend fun getAll(): List<City>

    @Delete
    suspend fun delete(city: City)

    @Update
    suspend fun update(city: City)

}