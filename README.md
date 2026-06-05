# MVL Map Booking Android Application

This is a premium, production-ready Android application built with **Jetpack Compose** and **Clean Architecture**. It implements a complete map booking flow with air quality information, nickname management, booking confirmation, and booking history.

---

## Setup Instructions

Follow these steps to set up and run the application locally:

### 1. Set Up Google Maps API Key
For security reasons, the Google Maps API Key is not checked into version control. It is dynamically loaded from your local-only `local.properties` file.

1. Open `local.properties` in the root of the project.
2. Add the following line at the end, replacing `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual Google Maps API Key:
   ```properties
   MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY_HERE
   ```
3. Sync your Gradle project. Gradle will read this key and inject it into the `AndroidManifest.xml` at build time.

### 2. Build the Application
Ensure you have Android Studio installed. Run the following command in the terminal to verify the project builds successfully:
```bash
./gradlew assembleDebug
```

### 3. Run Unit Tests
To run the project's test suite, execute:
```bash
./gradlew test
```

---

## 🏗 Architecture & Package Structure

The project follows **Clean Architecture** patterns combined with Hilt for Dependency Injection:

```text
com.amitraj.mvlassignment/
│
├── data/                         # Data Layer
│   ├── api/                      # Retrofit Interfaces & Mock APIs (MockBookingApiService, WAQI API)
│   ├── db/                       # Room Database & DAOs for local caching (LocationCacheDao)
│   ├── dto/                      # Network Data Transfer Objects
│   └── repository/               # Repository Implementations (LocationRepositoryImpl, BookingRepositoryImpl)
│
├── domain/                       # Domain Layer (Pure Kotlin, Framework Independent)
│   ├── model/                    # Pure Domain Models (Location, Book)
│   └── repository/               # Repository Interfaces defining API contracts
│
├── presentation/                 # Presentation Layer (UI and States)
│   ├── navigation/               # NavHost Routing and Sealed Screen routes (AppNavigation)
│   ├── screen/                   # Jetpack Compose Screens (Map, Nickname, Confirmation, History)
│   ├── uistate/                  # Immutable UI States
│   └── viewmodel/                # Jetpack Compose ViewModels (Hilt injected)
│
└── di/                           # Dependency Injection Modules (ApiModule, RepositoryModule, DatabaseModule)
```

---

## ✨ Features Implemented

1. **Map Booking Screen (Screen 1)**:
   - Live Google Maps rendering (Google Maps Compose).
   - Dragging the map updates center coordinates in real-time, fetching the reverse-geocoded address and local AQI.
   - Smart location row labels that resolve nicknames dynamically from Room Database cache.
   - Clean state machine buttons: `Set A` → `Set B` → `Book`.

2. **Nickname Screen (Screen 2)**:
   - Input length validation (max 20 characters).
   - Optional nickname saving (saving empty/blank input clears the nickname).
   - Pops back and immediately applies the nickname changes to the map screen.

3. **Booking Confirmation (Screen 3)**:
   - Displays booked location details A, B, and the final price returned by the API.
   - Handled back button clicks via `BackHandler` to reset the booking state and return to Screen 1.

4. **Booking History (Screen 4)**:
   - Shows summary stats: total count of bookings and total spent.
   - Displays a list of all successful bookings saved in the session.
   - Clickable booking items: selecting a past booking reloads it back into Screen 1 and centers the map view.

---