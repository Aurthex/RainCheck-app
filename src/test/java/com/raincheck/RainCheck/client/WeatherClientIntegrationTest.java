package com.raincheck.RainCheck.client;

import com.raincheck.RainCheck.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class WeatherClientIntegrationTest {
    WeatherClient client;
    Weather weather;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
        client = new WeatherClient("0", "0", currentDate);
        weather = new Weather(client.findDaily());
    }

    @Test
    void weatherExists() throws IOException, InterruptedException {
        assertNotNull(weather);
    }

    @Test
    void weatherTemperatureExists(){
        assertNotNull(weather.getTemperature());
    }

    @Test
    void weatherWeatherCodeExists(){
        assertNotNull(weather.getWeather_code());
    }

    @Test
    void weatherWindSpeedExists(){
        assertNotNull(weather.getWind_speed());
    }

    @Test
    void weatherStringContainsNoNullValues(){
        assertFalse(weather.toString().contains("null"));
    }
}