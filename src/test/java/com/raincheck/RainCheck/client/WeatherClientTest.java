package com.raincheck.RainCheck.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WeatherClientTest {
    WeatherClient client;

    @BeforeEach
    void setUp() {
        client = new WeatherClient("0", "0");
    }

    @Test
    void statusCodeIs200() throws IOException, InterruptedException {
        assertEquals(200, client.checkStatusCode());
    }

    @Test
    void findAllGetsAny() throws IOException, InterruptedException {
        String all = client.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void findAllHasEnclosedBraces() throws IOException, InterruptedException {
        String all = client.findAll();
        assertTrue(all.charAt(0) == '{' && all.charAt(all.length()-1) == '}');
    }

    @Test
    void findAllHasCorrectCommaCount() throws IOException, InterruptedException {
        String all = client.findAll();
        String[] lines = all.split(",");
        assertEquals(17, lines.length);
    }

    @Test
    void findAllHasKeywords() throws IOException, InterruptedException {
        String all = client.findAll();
        assertTrue(all.contains("latitude"));
        assertTrue(all.contains("longitude"));
        assertTrue(all.contains("timezone"));
        assertTrue(all.contains("current_units"));
        assertTrue(all.contains("current"));
        assertTrue(all.contains("time"));
    }

    @Test
    void findCurrentGetsAny() throws IOException, InterruptedException {
        String current = client.findCurrent();
        assertFalse(current.isEmpty());
    }

    @Test
    void findCurrent() throws IOException, InterruptedException {
        String current = client.findCurrent();
        assertTrue(current.charAt(0) == '{' && current.charAt(current.length()-1) == '}');
    }

    @Test
    void findCurrentHasCorrectCommaCount() throws IOException, InterruptedException {
        String current = client.findCurrent();
        String[] lines = current.split(",");
        assertEquals(5, lines.length);
    }

    @Test
    void findCurrentHasKeywords() throws IOException, InterruptedException {
        String current = client.findCurrent();
        assertTrue(current.contains("time"));
        assertTrue(current.contains("temperature_2m"));
        assertTrue(current.contains("weather_code"));
        assertTrue(current.contains("wind_speed_10m"));
    }
}