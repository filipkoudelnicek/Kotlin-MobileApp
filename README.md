# Movie Explorer

Android aplikace vyvinutá v Kotlinu pro vyhledávání a prohlížení informací o filmech pomocí OMDb API. Aplikace využívá moderní Jetpack Compose UI a následuje MVVM architekturu s Repository patternem.

## 📱 Funkce

- **Vyhledávání filmů** - Vyhledávání filmů přes OMDb API s real-time vyhledáváním
- **Detail filmu** - Zobrazení detailních informací o filmu (název, rok, režisér, žánr, popis, plakát)
- **Oblíbené filmy** - Ukládání oblíbených filmů do lokální Room databáze
- **Tmavý režim** - Přepínání mezi světlým a tmavým motivem aplikace
- **Splash screen** - Úvodní obrazovka při spuštění aplikace
- **Moderní UI** - Material 3 design s Jetpack Compose

## 🛠️ Technologie

- **Jazyk:** Kotlin (Coroutines, Flow)
- **UI:** Jetpack Compose + Navigation Compose + Material 3
- **Architektura:** MVVM s Repository pattern, AppContainer service locator
- **Síť:** Retrofit + Moshi, OMDb API
- **Databáze:** Room (oblíbené filmy)
- **Knihovny:** 
  - Coil pro načítání obrázků
  - AndroidX Splash Screen
  - KSP pro Room code generation

## 📁 Struktura projektu

```
app/
 ├─ data/              // DTOs, Retrofit služby, Room databáze, repository implementace, mappery
 ├─ domain/            // Domain modely, repository rozhraní
 ├─ ui/                // Compose obrazovky, ViewModely, navigace, theming
 │  ├─ screens/        // Hlavní obrazovky (Home, Detail, Favorites, Settings)
 │  ├─ navigation/     // Navigační logika
 │  └─ theme/          // Barevná schémata a typografie
 └─ MovieExplorerApp.kt // Application třída s AppContainer
```

## 📋 Požadavky

- Android Studio Ladybug (nebo novější) s AGP 8.7+
- JDK 17
- Android zařízení/emulátor s API 24+
- OMDb API klíč (zdarma na https://www.omdbapi.com/apikey.aspx)

## 🚀 Instalace a build

1. **Naklonujte repozitář**
   ```bash
   git clone https://github.com/filipkoudelnicek/Kotlin-MobileApp.git
   cd Kotlin-MobileApp
   ```

2. **Přidejte OMDb API klíč**
   - Otevřete `app/build.gradle.kts`
   - Najděte `buildConfigField("String", "OMDB_API_KEY", ...)`
   - Nahraďte aktuální hodnotu svým API klíčem:
     ```kotlin
     buildConfigField("String", "OMDB_API_KEY", "\"VÁŠ_API_KLÍČ\"")
     ```

3. **Synchronizujte a sestavte projekt**
   - Otevřete projekt v Android Studio
   - Synchronizujte Gradle soubory
   - Spusťte aplikaci na emulátoru nebo zařízení (`Shift+F10`)

## 💡 Použití

- **Vyhledávání:** Zadejte název filmu do vyhledávacího pole na domovské obrazovce
- **Detail filmu:** Klepněte na výsledek vyhledávání pro zobrazení detailních informací
- **Oblíbené:** Přidávejte filmy do oblíbených pomocí ikony hvězdy v seznamu výsledků nebo na detailu filmu
- **Nastavení:** Otevřete nastavení z domovské obrazovky pro přepnutí tmavého režimu

## 📝 Verze

- **Version Code:** 1
- **Version Name:** 1.0
- **Min SDK:** 24
- **Target SDK:** 35
- **Compile SDK:** 35

## 📄 Licence

Tento projekt je určen pro vzdělávací účely. Před distribucí buildů nahraďte OMDb API klíč vlastním.


