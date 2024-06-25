package com.raincheck.RainCheck.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

public class UserDataTest {

    private UserData userData;

    @BeforeEach
    public void setUp() {
        userData = new UserData();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(userData.getId()); // Assert id is null
        assertNull(userData.getLongitude()); // Assert longitude is null
        assertNull(userData.getLatitude()); // Assert latitude is null
        assertNull(userData.getDate()); // Assert date is null
        assertNull(userData.getLocation()); // Assert location is null
    }

    @Test
    public void testAllArgsConstructor() {
        UserData userData = new UserData();
        userData.setId(1);
        userData.setLongitude("10.12345");
        userData.setLatitude("20.54321");
        userData.setDate(Date.valueOf("2024-06-25"));
        userData.setLocation("Test Location");

        assertEquals(1, userData.getId()); // Assert id is 1
        assertEquals("10.12345", userData.getLongitude()); // Assert longitude is "10.12345"
        assertEquals("20.54321", userData.getLatitude()); // Assert latitude is "20.54321"
        assertEquals(Date.valueOf("2024-06-25"), userData.getDate()); // Assert date is 2024-06-25
        assertEquals("Test Location", userData.getLocation()); // Assert location is "Test Location"
    }

    @Test
    public void testGettersAndSetters() {
        userData.setId(1);
        userData.setLongitude("10.12345");
        userData.setLatitude("20.54321");
        userData.setDate(Date.valueOf("2024-06-25"));
        userData.setLocation("Test Location");

        assertEquals(1, userData.getId()); // Assert id is 1
        assertEquals("10.12345", userData.getLongitude()); // Assert longitude is "10.12345"
        assertEquals("20.54321", userData.getLatitude()); // Assert latitude is "20.54321"
        assertEquals(Date.valueOf("2024-06-25"), userData.getDate()); // Assert date is 2024-06-25
        assertEquals("Test Location", userData.getLocation()); // Assert location is "Test Location"
    }
}