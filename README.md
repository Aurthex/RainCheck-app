# RainCheck Application

## Overview

RainCheck is a web application designed to help users plan activities based on weather conditions. It provides functionality to manage activities, view weather forecasts, and adjust user settings.


## Features

- **Index**: Shows current user settings, weather information, and a list of recommended activities.
- **User Settings**: Allows users to update their location based on postcode.
- **Activities Management**: Supports adding, editing, deleting, and booking activities.
- **Weather Integration**: Retrieves and displays weather data relevant to activities.
- **Activity Conditions**: Allows defining conditions (general weather, temperature, wind speed) for each activity.
- **Date Management**: Automatically adjusts activity bookings based on the current date.

## Technologies Used

- **Java**: Backend language using Spring Framework (Spring Boot, Spring MVC).
- **HTML/CSS/JavaScript**: Frontend development for UI/UX.
- **Thymeleaf**: Template engine for server-side rendering.
- **PostgreSQL**: Database for storing user data, activities, and conditions.
- **<a href="https://open-meteo.com/en/docs">Weather Forecast API</a>**: Integrates weather data via external API.

## Setup Instructions
### Clone Repository:

```bash
git clone https://github.com/Aurthex/RainCheck-app.git
cd RainCheck
```

### Database Configuration:

1. Create a PostgreSQL database.
2. Update application-dev.properties with your database configuration:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
```
### Build and Run Application:

Build the project using Maven:
```go
mvn clean package
```
Run the application:
```
mvn spring-boot:run
```
Alternatively, run from an IDE like IntelliJ.

## Access Application:

Open a web browser and go to http://localhost:8080/ to access the RainCheck application.

## Usage
- Home Page: Displays user settings, current weather, recommended activities, and date selection.

- Activities Page: Lists all activities with options to add, edit, delete, and book activities.

- Add/Edit Activity: Fill in details and select conditions for each activity.

- User Settings: Update location settings based on postcode.

## Contributors
- <a href="https://github.com/marklovejoydev">Mark Lovejoy</a> - Developer
- <a href="https://github.com/Aurthex">Sam Arrowsmith</a> - Developer
- <a href="https://github.com/paulineldb">Pauline Lelievre Du Broeuille</a> - Developer
- <a href="https://github.com/shirinooo">Shirin Ebrahimi</a> - Developer

## Notes
Make sure to replace placeholders (like your_database) with actual values specific to your setup.