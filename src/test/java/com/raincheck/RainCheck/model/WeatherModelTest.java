package com.raincheck.RainCheck.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WeatherModelTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        Weather weather = new Weather(); // Instantiate Weather object using default constructor
        assertNull(weather.getTemperature()); // Assert temperature is null
        assertNull(weather.getWeather_code()); // Assert weather_code is null
        assertNull(weather.getWind_speed()); // Assert wind_speed is null
    }

    // Test all-args constructor
    @Test
    public void testAllArgsConstructor() {
        Weather weather = new Weather(23, 3, 5); // Instantiate Weather object using all-args constructor
        assertEquals(23, weather.getTemperature()); // Assert temperature is 23
        assertEquals(3, weather.getWeather_code()); // Assert weather_code is 3
        assertEquals(5, weather.getWind_speed()); // Assert wind_speed is 5
    }

    // Test JSON constructor
    @Test
    public void testJsonConstructor() {
        // JSON string with weather data
        String json = "{\"time\":[\"2024-06-25\"],\"weather_code\":[3],\"temperature_2m_max\":[27.4],\"wind_speed_10m_max\":[14.8]}";
        Weather weather = new Weather(json); // Instantiate Weather object using JSON constructor
        assertEquals(27, weather.getTemperature());
        assertEquals(3, weather.getWeather_code());
        assertEquals(15, weather.getWind_speed());
    }

    // Test toString method
    @Test
    public void testToString() {
        Weather weather = new Weather(23, 3, 5); // Instantiate Weather object using all-args constructor
        // Expected string representation of the Weather object
        String expected = "Weather: \nTemperature: \n23Code: \n3Wind Speed: \n5";
        assertEquals(expected, weather.toString()); // Assert the toString method returns the expected string
    }
    @Test
    public void testRounding() {
        String json = "{\"time\":[\"2024-06-25\"],\"weather_code\":[3],\"temperature_2m_max\":[27.6],\"wind_speed_10m_max\":[14.2]}";
        Weather weather = new Weather(json);
        assertEquals(28, weather.getTemperature()); // Assert temperature is correctly rounded up
        assertEquals(3, weather.getWeather_code()); // Assert weather_code is 3
        assertEquals(14, weather.getWind_speed()); // Assert wind_speed is correctly rounded down
    }
}