package com.raincheck.RainCheck.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

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
}