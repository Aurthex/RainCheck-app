package com.raincheck.RainCheck.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ActivityModelTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        // Arrange
        Activity activity = new Activity(); // Instantiate Activity object using default constructor

        // Act & Assert
        assertNull(activity.getId()); // Assert id is null
        assertNull(activity.getName()); // Assert name is null
        assertNull(activity.getDescription()); // Assert description is null
        assertNull(activity.getConditions()); // Assert conditions is null
    }

    // Test all-args constructor
    @Test
    public void testAllArgsConstructor() {
        // Arrange
        Condition[] conditions = new Condition[2];
        conditions[0] = new Condition(1, "Sunny", 0);
        conditions[1] = new Condition(2, "Clear", 0);

        // Act
        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);
        activity.setTemperature(25);
        activity.setWindSpeed(10);

        // Assert
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
        // Arrange
        Activity activity = new Activity(); // Instantiate Activity object using default constructor

        // Act
        Condition[] conditions = new Condition[1];
        conditions[0] = new Condition(1, "Clear", 0);

        activity.setId(1);
        activity.setName("Running");
        activity.setDescription("Outdoor running activity");
        activity.setConditions(conditions);
        activity.setDateScore(100);

        // Assert
        assertEquals(1, activity.getId()); // Assert id is 1
        assertEquals("Running", activity.getName()); // Assert name is "Running"
        assertEquals("Outdoor running activity", activity.getDescription()); // Assert description is "Outdoor running activity"
        assertEquals(conditions, activity.getConditions()); // Assert conditions is the same array
        assertEquals(100,activity.getDateScore());
    }

    // Test toJson method
    @Test
    public void testToJsonMethod() {
        // Arrange
        Condition[] conditions = new Condition[2];
        conditions[0] = new Condition(1, "Sunny", 0);
        conditions[1] = new Condition(2, "Clear", 0);

        // Act
        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);
        activity.setTemperature(25);
        activity.setWindSpeed(10);

        String json = activity.toJson();

        // Assert
        assertTrue(json.contains("\"name\":\"Running\""));
        assertTrue(json.contains("\"description\":\"Outdoor running activity\""));
        assertTrue(json.contains("\"temperature\":\"25\""));
        assertTrue(json.contains("\"windSpeed\":\"10\""));
        assertTrue(json.contains("\"conditions\":[{\"name\":\"Sunny\"},{\"name\":\"Clear\"}]"));
    }

    // Test displayInteger method
    @Test
    public void testDisplayInteger() {
        // Arrange
        Activity activity = new Activity();

        // Act & Assert
        Integer temperature = 25;
        assertEquals("25", activity.displayInteger(temperature)); // Test with non-null integer

        Integer windSpeed = null;
        assertEquals("any", activity.displayInteger(windSpeed)); // Test with null integer
    }

    // Test generateWeatherComparisons method with non-null conditions
    @Test
    public void testGenerateWeatherComparisons_WithNonNullConditions() {
        // Arrange
        Condition[] conditions = {
                new Condition(1, "Sunny", 0),
                new Condition(2, "Clear", 0)
        };
        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);
        activity.setTemperature(20);
        activity.setWindSpeed(5);

        Weather weather = new Weather(1, 1, 25); // Example weather

        // Act
        activity.generateWeatherComparisons(weather);

        // Assert
        assertNotNull(activity.getScore());
        assertNotNull(activity.getScoreMessage());
        assertTrue(activity.getScore() >= 0 && activity.getScore() <= 100); // Score should be between 0 and 100
        assertNotNull(activity.getScoreMessage());
    }

    // Test generateWeatherComparisons method with empty conditions
    @Test
    public void testGenerateWeatherComparisons_WithEmptyConditions() {
        // Arrange
        Activity activity = new Activity(1, "Running", "Outdoor running activity", new Condition[0]);

        Weather weather = new Weather(1, 1, 25); // Example weather

        // Act
        activity.generateWeatherComparisons(weather);

        // Assert
        assertNotNull(activity.getScore());
        assertNotNull(activity.getScoreMessage());
        assertTrue(activity.getScore() >= 0 && activity.getScore() <= 100); // Score should be between 0 and 100
        assertNotNull(activity.getScoreMessage());
    }

    // Test getConditionString method
    @Test
    public void testGetConditionString() {
        // Arrange
        Condition[] conditions = {
                new Condition(1, "Sunny", 0),
                new Condition(2, "Clear", 0)
        };
        Activity activity = new Activity(1, "Running", "Outdoor running activity", conditions);

        // Act
        String conditionString = activity.getConditionString();

        // Assert
        assertTrue(conditionString.contains("Sunny"));
        assertTrue(conditionString.contains("Clear"));
    }

    // Test getTempStatement method with temperature difference within limit
    @Test
    public void testGetTempStatement_WithTemperatureDifferenceWithinLimit() {
        // Arrange
        Activity activity = new Activity();
        activity.setTemperature(25);

        Weather weather = new Weather(25, 1, 27); // Warmer weather

        // Act
        int score = activity.getTempStatement(weather);

        // Assert
        assertTrue(score > 0 && score <= 10); // Score should be within the range [1, 10]
    }

    // Test getTempStatement method with temperature difference exceeding limit
    @Test
    public void testGetTempStatement_WithTemperatureDifferenceExceedingLimit() {
        // Arrange
        Activity activity = new Activity();
        activity.setTemperature(25);

        Weather weather = new Weather(1, 1, 10); // Much cooler weather

        // Act
        int score = activity.getTempStatement(weather);

        // Assert
        assertEquals(0, score); // Score should be 0 as temperature difference exceeds the limit
    }

    // Test getTempStatement method with null temperature
    @Test
    public void testGetTempStatement_WithNullTemperature() {
        // Arrange
        Activity activity = new Activity(); // Temperature is null by default

        Weather weather = new Weather(1, 1, 27);

        // Act
        int score = activity.getTempStatement(weather);

        // Assert
        assertEquals(0, score); // Score should be 0 when temperature is null
    }

    // Test getSpeedStatement method with wind speed difference within limit
    @Test
    public void testGetSpeedStatement_WithWindSpeedDifferenceWithinLimit() {
        // Arrange
        Activity activity = new Activity();
        activity.setWindSpeed(10);

        Weather weather = new Weather(1, 1, 25); // Higher wind speed

        // Act
        int score = activity.getSpeedStatement(weather);

        // Assert
        assertTrue(score > 0 && score <= 5); // Score should be within the range [1, 5]
    }

    // Test getSpeedStatement method with wind speed difference exceeding limit
    @Test
    public void testGetSpeedStatement_WithWindSpeedDifferenceExceedingLimit() {
        // Arrange
        Activity activity = new Activity();
        activity.setWindSpeed(0);

        Weather weather = new Weather(1, 1, 25); // Much higher wind speed

        // Act
        int score = activity.getSpeedStatement(weather);

        // Assert
        assertEquals(0, score); // Score should be 0 as wind speed difference exceeds the limit
    }

    // Test getSpeedStatement method with null wind speed
    @Test
    public void testGetSpeedStatement_WithNullWindSpeed() {
        // Arrange
        Activity activity = new Activity(); // Wind speed is null by default

        Weather weather = new Weather(1, 1, 25);

        // Act
        int score = activity.getSpeedStatement(weather);

        // Assert
        assertEquals(0, score); // Score should be 0 when wind speed is null
    }
    // Test getScoreMessage method for perfect match (score = 100)
    @Test
    public void testGetScoreMessage_PerfectMatch() {
        // Arrange
        Activity activity = new Activity();

        // Act
        String message = activity.getScoreMessage(100);

        // Assert
        assertEquals("The weather is a perfect match!", message);
    }

    // Test getScoreMessage method for ideal activity conditions (score = 90)
    @Test
    public void testGetScoreMessage_IdealForActivity() {
        // Arrange
        Activity activity = new Activity();

        // Act
        String message = activity.getScoreMessage(90);

        // Assert
        assertEquals("The weather is ideal for your activity.", message);
    }

    // Test getScoreMessage method for weather close to desired conditions (score = 80)
    @Test
    public void testGetScoreMessage_CloseToWhatYouWant() {
        // Arrange
        Activity activity = new Activity();

        // Act
        String message = activity.getScoreMessage(80);

        // Assert
        assertEquals("The weather is close to what you want.", message);
    }

    // Test getScoreMessage method for borderline acceptable conditions (score = 60)
    @Test
    public void testGetScoreMessage_CouldMakeThisWork() {
        // Arrange
        Activity activity = new Activity();

        // Act
        String message = activity.getScoreMessage(60);

        // Assert
        assertEquals("You could maybe make this work.", message);
    }

    // Test getScoreMessage method for unsuitable weather conditions (score = 40)
    @Test
    public void testGetScoreMessage_DoSomethingElse() {
        // Arrange
        Activity activity = new Activity();

        // Act
        String message = activity.getScoreMessage(40);

        // Assert
        assertEquals("Maybe you should do something else.", message);
    }

}