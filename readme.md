# Tamil Nadu Bus Tracker App

## Project Overview

The Tamil Nadu Bus Tracker is a real-time bus tracking Android application designed to provide users with efficient and accurate information about bus services across Tamil Nadu. This application aims to enhance the daily commute experience by offering features such as searching for buses between specific stops, viewing bus schedules, and tracking the live location of buses. For administrative purposes, it also includes functionalities for data insertion and management of bus routes and stops.

## Features

* **Bus Search & Listing**: Users can search for buses by specifying "From" and "To" destinations and view a list of available buses that serve the route.
* **Real-time Bus Tracking**: Track the live location and status of a selected bus on its route, with visual indicators for stops where the bus has arrived or departed.
* **Location-based Updates**: Utilizes the device's GPS to provide real-time location updates of buses to the Firebase Realtime Database.
* **Voice Announcements**: Integrates Text-to-Speech (TTS) to announce bus arrival and departure statuses at stops.
* **Data Management (Admin)**: Provides an interface for authorized personnel to insert and manage district names, bus details (name, type), bus stops, and their corresponding latitude and longitude coordinates.
* **Interactive Map View**: Displays bus locations on a Google Map, allowing users to visualize the bus's movement and stop locations.
* **User-friendly Interface**: Intuitive design with auto-completing text views for easy stop selection and clear visual feedback for bus status.
* **Splash Screen**: A welcoming splash screen upon application launch featuring the Tamil Nadu Government logo and a loading animation.
* **User Profile/Login (Planned)**: A dedicated screen for user login/profile settings, currently designed for inputting user details like name, district, gender, and phone number.

## Technical Architecture

The application follows a client-server architecture primarily leveraging **Firebase Realtime Database** as its backend.

### Key Components:

1.  **Android Client (Frontend)**:
    * Developed in **Java** and **XML**.
    * **Activities**:
        * `MainActivity`: Serves as the splash screen.
        * `Bus_list`: Handles bus search functionality and displays search results.
        * `Track_bus`: Manages real-time bus tracking, location updates, and voice announcements.
        * `Insert_data`: Provides administrative interface for data entry into the database.
        * `Map_activity`: Displays Google Maps and handles map interactions.
        * `Login`: A placeholder for user authentication/profile management.
    * **Adapters**:
        * `Bus_adapter`: Custom `BaseAdapter` for displaying bus details in the search results list.
        * `Track_adapter`: Custom `BaseAdapter` for visually representing bus stops and the bus's position on the tracking route.
    * **Google Maps API**: Integrated for displaying geographical locations and bus routes.
    * **Google Location Services API (FusedLocationProviderClient)**: Used in `Track_bus` for precise and efficient location updates of the device acting as a bus.
    * **Text-to-Speech (TTS)**: Implemented to provide audio alerts for bus movements.
    * **Lottie Animation Library**: Utilized for engaging animations, such as loading indicators and "no results" feedback.

2.  **Firebase Realtime Database (Backend)**:
    * **Centralized Data Storage**: All bus information (names, types, locations, statuses), bus stops, and search-related data are stored here.
    * **Real-time Synchronization**: Firebase's real-time capabilities are crucial for instantly updating bus locations and statuses across all connected clients.
    * **Data Structure**:
        * `District/Erode/Bus`: Stores individual bus details (e.g., `Bus_name`, `Bus_type`, `Location` (lat/lon), `status` (real_status/check_status)).
        * `District/Erode/Bus_stop`: Contains a list of all bus stops with their `stop` name, `lat`, and `lon`.
        * `District/Erode/Search`: Links bus names to the stops they serve (`bus_name`, `stop_name`), facilitating route searches.
    * **Offline Capabilities**: Firebase persistence is enabled (`FirebaseDatabase.getInstance().setPersistenceEnabled(true)`), allowing the app to function even without a constant internet connection, caching data locally.

### Data Flow & Interaction:

1.  **Data Insertion**: Admin users enter bus and stop details via `Insert_data` activity, which writes directly to the Firebase Realtime Database.
2.  **Bus Search**:
    * In `Bus_list`, users input "From" and "To" stops.
    * The app queries `District/Erode/Search` to find buses that serve both selected stops.
    * Matching bus details are then fetched from `District/Erode/Bus` and displayed using `Bus_adapter`.
3.  **Bus Tracking**:
    * When a user selects a bus, `Track_bus` activity starts.
    * It retrieves all stops for that bus from `District/Erode/Search`.
    * If location tracking is enabled (via a `Switch`), the device's `FusedLocationProviderClient` continuously updates the bus's `Location` (lat/lon) and `status` (in/out of stop) in Firebase based on proximity to defined stop coordinates.
    * Other users tracking the same bus receive real-time updates from Firebase, and the `Track_adapter` visually adjusts the bus's position on the route.
    * Text-to-Speech announces status changes.

## Setup and Installation

To set up and run this project:

1.  **Clone the Repository**:
    ```bash
    git clone <repository_url>
    cd TamilNaduBusTracker
    ```
2.  **Open in Android Studio**:
    * Open the cloned project in Android Studio.
3.  **Set up Firebase**:
    * Create a new Firebase project in the Firebase Console.
    * Add an Android app to your Firebase project. Follow the setup wizard to:
        * Register your app's package name (`com.example.tamilnadubustracker`).
        * Download the `google-services.json` file.
        * Place `google-services.json` in your app module's root directory.
    * Enable **Realtime Database** in your Firebase project. Set its security rules to allow read/write for testing purposes (e.g., `".read": "true", ".write": "true"`). **Note**: For production, implement proper authentication and security rules.
4.  **Google Maps API Key**:
    * Enable the **Maps SDK for Android** in Google Cloud Console for your project.
    * Obtain an API key.
    * Add your API key to your `AndroidManifest.xml` (or `local.properties` for better security practices if not already done):
        ```xml
        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="YOUR_Maps_API_KEY" />
        ```
5.  **Build and Run**:
    * Sync the project with Gradle files.
    * Connect an Android device or use an emulator.
    * Run the application from Android Studio.

## Technologies Used

* **Android SDK**
* **Java**
* **Firebase Realtime Database**
* **Google Maps SDK for Android**
* **Google Location Services**
* **Lottie (Airbnb)** for animations
* **ConstraintLayout** for UI design
* **Material Design Components**

## Future Enhancements

* Implement robust user authentication (Login/Registration).
* Improve Firebase security rules for production deployment.
* Add push notifications for bus status updates.
* Allow users to save favorite routes and buses.
* Implement real-time fare information.
* Improve map interactivity to show multiple buses or selected routes.
* Optimize location updates for battery efficiency.

---

**Developed by:** [Ranjith kumar AK]
**Contact:** [akranjithkumar03@gmail.com]
