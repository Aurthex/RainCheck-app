package com.raincheck.RainCheck.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeatherTest {
    public Weather weather;

    @Before
    public void setUp(){
        weather = new Weather(20, 0, 8);
    }

    @Test
    public void weatherHasTemperature() {assertEquals(weather.getTemperature().intValue(), 20);}

    @Test
    public void weatherHasWeatherCode() {assertEquals(weather.getWeather_code().intValue(), 0);}

    @Test
    public void weatherHasWindSpeed() {assertEquals(weather.getWind_speed().intValue(), 8);}

    @Test
    public void weatherHasToStringMethod() {
        assertEquals(weather.toString(),
                "Weather: " + "\n" +
                "Temperature: " + "\n" + 20 +
                "Code: " + "\n" + 0 +
                "Wind Speed: " + "\n" + 8);
    }
    // Additional tests for setters
    @Test
    public void testTemperatureSetter() {
        weather.setTemperature(25);
        assertEquals(25, weather.getTemperature().intValue());
    }

    @Test
    public void testWeatherCodeSetter() {
        weather.setWeather_code(2);
        assertEquals(2, weather.getWeather_code().intValue());
    }

    @Test
    public void testWindSpeedSetter() {
        weather.setWind_speed(10);
        assertEquals(10, weather.getWind_speed().intValue());
    }
}