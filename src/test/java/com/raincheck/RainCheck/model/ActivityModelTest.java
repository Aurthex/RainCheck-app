package com.raincheck.RainCheck.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ActivityModelTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        Activity activity = new Activity(); // Instantiate Activity object using default constructor
        assertNull(activity.getId()); // Assert id is null
        assertNull(activity.getName()); // Assert name is null
        assertNull(activity.getDescription()); // Assert description is null
        assertNull(activity.getConditions()); // Assert conditions is null
    }

    // Test all-args constructor
    @Test
    public void testAllArgsConstructor() {
        Condition[] conditions = new Condition[2];
        conditions[0] = new Condition(1, "Sunny", 0);
        conditions[1] = new Condition(2, "Clear", 0);

        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);
        activity.setTemperature(25);
        activity.setWindSpeed(10);

        // Assert all fields set correctly
        assertEquals(1, activity.getId()); // Assert id is 1
        assertEquals("Running", activity.getName()); // Assert name is "Running"
        assertEquals("Outdoor running activity", activity.getDescription()); // Assert description is "Outdoor running activity"
        assertEquals(25, activity.getTemperature()); // Assert temperature is 25
        assertEquals(10, activity.getWindSpeed()); // Assert windSpeed is 10
        assertArrayEquals(conditions, activity.getConditions()); // Assert conditions array is the same
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        Activity activity = new Activity(); // Instantiate Activity object using default constructor

        // Create a new array of conditions
        Condition[] conditions = new Condition[1];
        conditions[0] = new Condition(1, "Clear", 0);

        // Set values using setters
        activity.setId(1);
        activity.setName("Running");
        activity.setDescription("Outdoor running activity");
        activity.setConditions(conditions);

        // Assert values using getters
        assertEquals(1, activity.getId()); // Assert id is 1
        assertEquals("Running", activity.getName()); // Assert name is "Running"
        assertEquals("Outdoor running activity", activity.getDescription()); // Assert description is "Outdoor running activity"
        assertEquals(conditions, activity.getConditions()); // Assert conditions is the same array
    }

    @Test
    public void testToJsonMethod() {
        Condition[] conditions = new Condition[2];
        conditions[0] = new Condition(1, "Sunny", 0);
        conditions[1] = new Condition(2, "Clear", 0);

        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);
        activity.setTemperature(25);
        activity.setWindSpeed(10);

        String json = activity.toJson();

        // Validate JSON structure and content
        assertTrue(json.contains("\"name\":\"Running\""));
        assertTrue(json.contains("\"description\":\"Outdoor running activity\""));
        assertTrue(json.contains("\"temperature\":\"25\""));
        assertTrue(json.contains("\"windSpeed\":\"10\""));

        // Validate conditions array
        assertTrue(json.contains("\"conditions\""));
        assertTrue(json.contains("\"name\":\"Sunny\""));
        assertTrue(json.contains("\"name\":\"Clear\""));
    }
    @Test
    public void testDisplayInteger() {
        Activity activity = new Activity();

        // Test with non-null integer
        Integer temperature = 25;
        assertEquals("25", activity.displayInteger(temperature));

        // Test with null integer
        Integer windSpeed = null;
        assertEquals("any", activity.displayInteger(windSpeed));
    }
}