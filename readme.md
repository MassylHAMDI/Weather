# 🌦️ Modern Weather App

A sleek weather application built with Jetpack Compose that demonstrates modern Android development practices.

## ✨ Features

- 🎨 Modern UI with frosted glass effects
- 🌦️ Real-time weather updates
- 📱 Material Design 3 components
- 🗃️ Local data persistence
- 🔄 API rate limiting

## 🛠️ Tech Stack

- **🎭 UI**: Jetpack Compose
- **🏗️ Architecture**: MVVM
- **💾 Database**: Room
- **🌐 Networking**: Retrofit
- **⚡ Asynchronous**: Kotlin Coroutines & Flow
- **🖼️ Image Loading**: Coil
- **🧭 Navigation**: Compose Navigation
- **💉 DI**: Manual Dependency Injection

## 📁 Project Structure

```
app/
├── data/
│   ├── api/
│   │   ├── WeatherAPI.kt         # API interface
│   │   └── RetrofitClient.kt     # Network client
│   ├── database/
│   │   ├── AppDatabase.kt        # Room database
│   │   ├── CityDao.kt           # Data access
│   │   └── LastUpdateDao.kt     # Update tracking
│   └── models/
│       ├── City.kt              # City entity
│       └── WeatherResponse.kt   # API response model
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt    # Main screen
│   │   │   └── WeatherCard.kt   # City card
│   │   └── detail/
│   │       └── DetailScreen.kt  # City details
│   ├── theme/
│   └── navigation/
```

## 🚀 Setup

1. Clone the repository
2. Add your OpenWeatherMap API key in `local.properties`:
```properties
WEATHER_API_KEY=your_api_key_here
```
3. Build and run

## 📱 Screens

### 🏠 Home Screen
- City list with current weather
- Add new cities
- Navigate to details
- Modern frosted glass UI
- Dynamic weather icons
- Real-time temperature updates
- Smooth animations

### 📊 Detail Screen
- Detailed weather information including:
  - Current temperature
  - Weather condition with icon
- Frosted glass card design
- Animated transitions
- Delete city option

## 📊 Data Models

### 🏙️ City Entity
```kotlin
@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    val name: String,
    val icon: String? = null,
    val temperature: Double? = null,
)
```

## 🌐 API Integration

Uses OpenWeatherMap API for weather data:
- Current weather endpoints
- Rate limited to 1 request per minute
- Local caching with Room database
- Last update tracking

## 🚀 Future Improvements

- [ ] 🔧 Weather widgets implementation
- [ ] 📅 5-day weather forecasts
- [ ] 🗺️ Interactive weather maps
- [ ] 📤 Weather data sharing
- [ ] 🌡️ Multiple temperature units
- [ ] ⚠️ Weather alerts
- [ ] 🎨 Custom themes
- [ ] 📱 Tablet optimization
- [ ] 📊 More detailed weather information

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.