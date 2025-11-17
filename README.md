# Movie Explorer (Android, Kotlin, MVVM)

Movie Explorer is a Jetpack Compose Android app that lets you search the OMDb API for movies, view detailed information, and save favourites locally via Room. It follows a modular MVVM architecture with repositories mediating between network and persistence layers.

## Tech Stack

- **Language:** Kotlin (Coroutines, Flow)
- **UI:** Jetpack Compose + Navigation Compose + Material 3
- **Architecture:** MVVM with Repository pattern, AppContainer service locator
- **Network:** Retrofit + Moshi, OMDb API
- **Persistence:** Room (favourite movies)
- **Device APIs:** Google Play Services Location (geolocation screen)
- **Other:** Coil for poster loading, AndroidX Splash Screen

## Project Structure

```
app/
 ├─ data/        // DTOs, Retrofit, Room, repositories, mappers
 ├─ domain/      // Domain models, repository interfaces
 ├─ ui/          // Compose screens, ViewModels, navigation, theming
 └─ MovieExplorerApp.kt // Application class with AppContainer
```

## Prerequisites

- Android Studio Ladybug (or newer) with AGP 8.5+
- JDK 17
- Android device/emulator running API 24+
- OMDb API key (free at https://www.omdbapi.com/apikey.aspx)

## Setup & Build

1. **Clone the repo**
   ```bash
   git clone https://github.com/filipkoudelnicek/Kotlin-MobileApp.git
   cd Kotlin-MobileApp
   ```
2. **Add OMDb API key**  
   - Open `app/build.gradle.kts`
   - Replace the placeholder in `buildConfigField("String", "OMDB_API_KEY", "\"YOUR_OMDB_API_KEY\"")`
3. **Sync & Build**  
   - Open the project in Android Studio  
   - Sync Gradle, then run `app` on an emulator or device (`Shift+F10`)
4. **Permissions**  
   - Location screen requests runtime permission the first time you open it. Allow coarse/fine location to display your coordinates.

## Features Checklist

- [x] OMDb search with pagination-ready endpoint
- [x] Movie details (title, year, director, genre, plot, poster)
- [x] Room-backed favourites list with toggles in search & detail
- [x] Geolocation screen using FusedLocationProvider
- [x] Splash screen + custom launcher icon

## Testing Tips

- Use the search screen to query e.g. “Star Wars”, tap a result to see details, toggle favourites via the star icons, and confirm the entry appears on the favourites screen.
- Open the location screen from the home top bar to verify permission requests and current coordinates.

## License

This project is provided for educational purposes. Replace the OMDb key with your own before distributing builds.


