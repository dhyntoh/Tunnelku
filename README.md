# NetMod HTTP Custom App

This is a modern Android application that provides HTTP request functionality similar to NetMod and HTTP Custom tools. The app features a futuristic UI with smooth animations and all the essential features for making HTTP requests.

## Features

- **HTTP Methods**: Support for GET, POST, PUT, DELETE requests
- **Custom Headers**: Add/remove multiple custom headers
- **Request Body**: Support for JSON request body
- **Response Display**: Pretty-printed JSON response with syntax highlighting
- **Response Info**: Status code, response time, and response size
- **Futuristic UI**: Dark theme with futuristic design elements
- **Real-time Updates**: Live response updates with loading states

## Technologies Used

- **Kotlin**: Modern Android development language
- **Material Design Components**: For beautiful UI elements
- **OkHttp**: For efficient HTTP networking
- **RecyclerView**: For dynamic header management
- **ConstraintLayout**: For flexible layouts
- **View Binding**: For type-safe view references

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/netmodapp/
│   │   ├── MainActivity.kt          # Main application logic
│   │   ├── adapters/
│   │   │   └── HeaderAdapter.kt     # RecyclerView adapter for headers
│   │   ├── models/
│   │   │   └── Header.kt            # Data model for headers
│   │   └── utils/
│   │       └── NetworkUtils.kt      # HTTP networking utilities
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml    # Main activity layout
│   │   │   └── item_header.xml      # Header item layout
│   │   └── values/
│   │       ├── strings.xml          # App strings
│   │       ├── colors.xml           # Color scheme
│   │       └── styles.xml           # UI styles
│   └── AndroidManifest.xml
```

## Setup Instructions

1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the project directory
4. Wait for Gradle sync to complete
5. Build and run the application

## Permissions

- `INTERNET`: Required for making HTTP requests
- `ACCESS_NETWORK_STATE`: For network status checks

## How to Use

1. Enter the URL you want to make a request to
2. Select the HTTP method (GET, POST, PUT, DELETE)
3. Add any custom headers using the "Add Header" button
4. Enter request body (for POST/PUT requests)
5. Click "Send Request" to make the HTTP call
6. View the response in the bottom card with status, time, and size information

## Customization

The app features a futuristic dark theme that can be customized by modifying:
- `colors.xml` - Color scheme
- `styles.xml` - UI component styles
- `activity_main.xml` - Layout design