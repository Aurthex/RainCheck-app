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
        Condition[] conditions = new Condition[0]; // Create an empty array of conditions
        // Instantiate Activity object using all-args constructor
        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);
        assertEquals(1, activity.getId()); // Assert id is 1
        assertEquals("Running", activity.getName()); // Assert name is "Running"
        assertEquals("Outdoor running activity", activity.getDescription()); // Assert description is "Outdoor running activity"
        assertEquals(conditions, activity.getConditions()); // Assert conditions is the same array
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
}