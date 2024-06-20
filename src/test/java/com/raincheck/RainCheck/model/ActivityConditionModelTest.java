package com.raincheck.RainCheck.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ActivityConditionModelTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        ActivityCondition activityCondition = new ActivityCondition(); // Instantiate ActivityCondition object using default constructor
        assertNull(activityCondition.getId()); // Assert id is null
        assertNull(activityCondition.getActivity()); // Assert activity is null
        assertNull(activityCondition.getCondition()); // Assert condition is null
    }

    // Test all-args constructor
    @Test
    public void testAllArgsConstructor() {
        Activity activity = new Activity(1, "Running", "Outdoor running activity", null); // Create a sample Activity object
        Condition condition = new Condition(1, "Clear", 0); // Create a sample Condition object
        // Instantiate ActivityCondition object using all-args constructor
        ActivityCondition activityCondition = new ActivityCondition(1, activity, condition);
        assertEquals(1, activityCondition.getId()); // Assert id is 1
        assertEquals(activity, activityCondition.getActivity()); // Assert activity is the same as the created Activity object
        assertEquals(condition, activityCondition.getCondition()); // Assert condition is the same as the created Condition object
    }

    // Test specific constructor
    @Test
    public void testSpecificConstructor() {
        Activity activity = new Activity(1, "Running", "Outdoor running activity", null); // Create a sample Activity object
        Condition condition = new Condition(1, "Clear", 0); // Create a sample Condition object
        // Instantiate ActivityCondition object using the specific constructor
        ActivityCondition activityCondition = new ActivityCondition(activity, condition);
        assertNull(activityCondition.getId()); // Assert id is null (since it's not set by this constructor)
        assertEquals(activity, activityCondition.getActivity()); // Assert activity is the same as the created Activity object
        assertEquals(condition, activityCondition.getCondition()); // Assert condition is the same as the created Condition object
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        ActivityCondition activityCondition = new ActivityCondition(); // Instantiate ActivityCondition object using default constructor

        // Create sample Activity and Condition objects
        Activity activity = new Activity(1, "Running", "Outdoor running activity", null);
        Condition condition = new Condition(1, "Clear", 0);

        // Set values using setters
        activityCondition.setId(1);
        activityCondition.setActivity(activity);
        activityCondition.setCondition(condition);

        // Assert values using getters
        assertEquals(1, activityCondition.getId()); // Assert id is 1
        assertEquals(activity, activityCondition.getActivity()); // Assert activity is the same as the created Activity object
        assertEquals(condition, activityCondition.getCondition()); // Assert condition is the same as the created Condition object
    }
}