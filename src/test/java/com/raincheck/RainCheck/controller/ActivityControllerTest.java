package com.raincheck.RainCheck.controller;

import com.raincheck.RainCheck.client.PostCodeClient;
import com.raincheck.RainCheck.client.WeatherClient;
import com.raincheck.RainCheck.model.*;
import com.raincheck.RainCheck.repository.ActivityConditionRepository;
import com.raincheck.RainCheck.repository.ActivityRepository;
import com.raincheck.RainCheck.repository.ConditionRepository;
import com.raincheck.RainCheck.repository.UserDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActivityController using Mockito for mocking repositories and model.
 */
@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityConditionRepository activityConditionRepository;

    @Mock
    private ConditionRepository conditionRepository;

    @Mock
    private Model model;

    @Mock
    private PostCodeClient postCodeClient;

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private ActivityController activityController;

    private Activity activity;
    private Condition condition;
    private ActivityCondition activityCondition;
    private UserData userData;
    private Weather weather;

    /**
     * Setup method to initialize mock objects and test data before each test method.
     */
    @BeforeEach
    public void setUp() {
        activity = new Activity(1, "Running", "Outdoor running", null);
        condition = new Condition(1, "Sunny", 100, "sun.png");
        activityCondition = new ActivityCondition(activity, condition);

        userData = new UserData();
        userData.setId(1);
        userData.setDate(Date.valueOf(LocalDate.now()));
        userData.setLatitude("52.5200");
        userData.setLongitude("13.4050");
    }

    @Test
    public void testShowActivities() throws IOException, InterruptedException {
        when(activityRepository.findAll()).thenReturn(Collections.singletonList(activity));
        when(activityConditionRepository.findByActivity(any(Activity.class))).thenReturn(Collections.singletonList(activityCondition));

        String viewName = activityController.showActivities(model);

        verify(model, times(1)).addAttribute("activities", Collections.singletonList(activity));
        assertEquals("activities/activities", viewName);
    }

    /**
     * Test method for creating activities.
     * Mocks repository calls to return predefined condition.
     * Verifies that activity and activity condition repositories are called once to save data.
     * Checks that the redirection view URL matches expected value.
     */

    @Test
    public void testAddNewActivity() {
        // Mock repository behavior
        when(conditionRepository.findAll()).thenReturn(Collections.singletonList(condition));

        // Call the method under test
        String viewName = activityController.addNewActivity(model);

        // Verify model attributes
        verify(model, times(1)).addAttribute(eq("activity"), any(Activity.class));
        verify(model, times(1)).addAttribute("conditions", Collections.singletonList(condition));
        assertEquals("activities/new", viewName);
    }

    @Test
    public void testGetActivitiesWithConditions() {
        // Mock repository behavior
        when(activityRepository.findAll()).thenReturn(Collections.singletonList(activity));
        when(activityConditionRepository.findByActivity(any(Activity.class))).thenReturn(Collections.singletonList(activityCondition));

        // Call the method under test
        List<Activity> activities = activityController.getActivitiesWithConditions();

        // Assertions
        assertEquals(1, activities.size());
        assertEquals(1, activities.get(0).getConditions().length);
        assertEquals("Sunny", activities.get(0).getConditions()[0].getName());
    }

    @Test
    public void testDeleteActivity() {
        // Mock repository behavior
        Integer activityId = 1;

        // Call the method under test
        RedirectView redirectView = activityController.deleteActivity(activityId);

        // Verify repository interaction
        verify(activityRepository, times(1)).deleteById(activityId);
        assertEquals("/activities", redirectView.getUrl());
    }

    @Test
    public void testEditActivity() {
        // Mock repository behavior
        when(activityRepository.findById(anyInt())).thenReturn(Optional.of(activity));
        when(conditionRepository.findAll()).thenReturn(Collections.singletonList(condition));

        // Call the method under test
        String viewName = activityController.editActivity(1, model);

        // Verify model attributes
        assertEquals("activities/new", viewName);
        verify(model, times(1)).addAttribute(eq("activity"), any(Activity.class));
        verify(model, times(1)).addAttribute(eq("conditions"), anyList());
    }

    @Test
    public void testGetWeatherIcon() throws Exception {
        weather = new Weather();
        weather.setWeather_code(100);
        // Mock the conditionRepository to return the mock condition
        when(conditionRepository.findByWeatherCode(100)).thenReturn(condition);

        // Use reflection to access the private method
        Method method = ActivityController.class.getDeclaredMethod("getWeatherIcon", Weather.class);
        method.setAccessible(true);

        // Invoke the private method
        String icon = (String) method.invoke(activityController, weather);

        // Verify the returned icon
        assertEquals("sun.png", icon);

        // Verify the interaction with the conditionRepository
        verify(conditionRepository, times(1)).findByWeatherCode(100);
    }

    @Test
    public void testAddNewActivities() {
        // Setup test data
        Activity activity = new Activity();
        activity.setId(1);
        activity.setName("Running");
        activity.setDescription("Outdoor running");

        Condition condition1 = new Condition();
        condition1.setId(1);
        condition1.setName("Sunny");
        condition1.setWeatherCode(100);
        condition1.setWeatherIcon("sun.png");

        Condition condition2 = new Condition();
        condition2.setId(2);
        condition2.setName("Rainy");
        condition2.setWeatherCode(200);
        condition2.setWeatherIcon("rain.png");

        activity.setConditions(new Condition[]{condition1, condition2});

        // Mock repository behavior
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);
        when(activityConditionRepository.findByActivity(any(Activity.class))).thenReturn(Collections.emptyList());

        // Call the method under test
        RedirectView redirectView = activityController.addNewActivity(activity);

        // Verify repository interactions
        verify(activityRepository, times(1)).save(activity);
        verify(activityConditionRepository, times(1)).findByActivity(activity);
        verify(activityConditionRepository, times(2)).save(any(ActivityCondition.class));

        // Verify redirection URL
        assertEquals("/activities", redirectView.getUrl());
    }

    @Test
    public void testCheckBooking() throws IOException, InterruptedException {
        // Mock dependencies
        Weather mockWeather = mock(Weather.class);
        Activity mockActivity = mock(Activity.class);

        // Use a spy to partially mock the activityController
        ActivityController spyController = spy(activityController);
        doReturn(mockWeather).when(spyController).getWeatherFromBooking(mockActivity);

        // Call the checkBooking method on the spy
        spyController.checkBooking(mockActivity);

        // Verify the interactions
        verify(spyController, times(1)).getWeatherFromBooking(mockActivity);
        verify(mockActivity, times(1)).generateWeatherComparisons(mockWeather);
    }

    @Test
    public void testRemoveBooking_NonExistingActivity() {
        // Mock repository behavior for non-existent activity
        Integer activityId = 1;
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        // Call the method under test
        RedirectView redirectView = activityController.removeBooking(activityId);

        // Verify redirection to /activities
        assertEquals("/activities", redirectView.getUrl());
        verify(activityRepository, never()).save(any());
    }

    @Test
    public void testRemoveBooking_ExistingActivity() {
        // Mock repository behavior for existing activity
        Integer activityId = 1;
        Activity mockActivity = mock(Activity.class); // Create a mock Activity instance
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(mockActivity)); // Return the mockActivity

        // Call the method under test
        RedirectView redirectView = activityController.removeBooking(activityId);

        // Verify redirection to /activities
        assertEquals("/activities", redirectView.getUrl());

        // Verify that mockActivity resetBooking and save methods are called once
        verify(mockActivity, times(1)).resetBooking(); // Verify the mockActivity
        verify(activityRepository, times(1)).save(mockActivity); // Verify the mockActivity is saved
    }

    @Test
    public void testBookActivity_ValidActivityId() throws IOException, InterruptedException {
        // Mock repository behavior for existing activity
        String activityId = "1";
        when(activityRepository.findById(Integer.parseInt(activityId))).thenReturn(Optional.of(activity));

        // Mock the behavior of getUserData method to return a mocked UserData object
        UserData userData = new UserData();
        userData.setId(1);
        userData.setDate(Date.valueOf(LocalDate.now()));
        userData.setLatitude("52.5200");
        userData.setLongitude("13.4050");

        // Use a spy to partially mock the activityController
        ActivityController spyController = spy(activityController);
        doReturn(userData).when(spyController).getUserData();

        // Call the method under test on the spy
        RedirectView redirectView = spyController.bookActivity(activityId);

        // Verify redirection to /
        assertEquals("/", redirectView.getUrl());

        // Verify that activity is updated and saved correctly
        verify(activityRepository, times(1)).save(activity);
    }

    @Test
    public void testBookActivity_InvalidActivityId() throws IOException, InterruptedException {
        // Mock repository behavior for non-existent activity
        String activityId = "1";
        when(activityRepository.findById(Integer.parseInt(activityId))).thenReturn(Optional.empty());

        // Call the method under test
        RedirectView redirectView = activityController.bookActivity(activityId);

        // Verify redirection to /
        assertEquals("/", redirectView.getUrl());

        // Verify that activity save method is never called
        verify(activityRepository, never()).save(any());
    }
    @Test
    public void testGetWeatherFromBooking() throws IOException, InterruptedException {
        // Mocked activity data
        Activity activity = new Activity();
        activity.setLatitude("52.5200");
        activity.setLongitude("13.4050");
        activity.setDate(Date.valueOf(LocalDate.now()));

        // Mocked Weather object
        Weather mockedWeatherData = new Weather(31, 3, 5);

        // Call the method under test
        Weather weather = activityController.getWeatherFromBooking(activity);

        // Verify that weather object is correctly instantiated with mocked data
        assertEquals(31, weather.getTemperature());
        assertEquals(3, weather.getWeather_code());

    }



    @Test
    public void testGetUserData() {
        // Mock the behavior of userDataRepository
        UserData userData = new UserData();
        userData.setId(1);
        userData.setDate(Date.valueOf(LocalDate.now().minusDays(1))); // Set the date to yesterday
        userData.setLatitude("52.5200");
        userData.setLongitude("13.4050");

        when(userDataRepository.findAll()).thenReturn(Collections.singletonList(userData));
        when(userDataRepository.save(any(UserData.class))).thenReturn(userData);

        // Use a spy to partially mock the activityController
        ActivityController spyController = spy(activityController);

        // Call the getUserData method
        UserData returnedUserData = spyController.getUserData();

        // Verify that the date was updated to today
        Date today = Date.valueOf(LocalDate.now());
        assertEquals(today, returnedUserData.getDate());

        // Verify the interaction with userDataRepository
        verify(userDataRepository, times(1)).findAll();
        verify(userDataRepository, times(1)).save(userData);
    }
    @Test
    public void testUpdateUserData_ValidPostcode() throws URISyntaxException, IOException, InterruptedException {

        // Call the method under test
        RedirectView redirectView = activityController.updateUserData(userData, "EC1A 1BB");

        // Verify that userData was updated correctly
        assertEquals("51.52456", userData.getLatitude());
        assertEquals("-0.11204", userData.getLongitude());
        assertEquals("Islington", userData.getLocation());

        // Verify redirection
        assertEquals("/", redirectView.getUrl());
    }
    //postcode test
    //postcode
    @Test
    public void testCleanPostcode_NormalInput() {
        // Test with a normal postcode input
        String input = "EC1A 1BB";
        String cleanedPostcode = activityController.cleanPostcode(input);
        assertEquals("EC1A1BB", cleanedPostcode);
    }

    @Test
    public void testCleanPostcode_WithQuotes() {
        // Test with input containing quotes
        String input = "\"EC1A 1BB\"";
        String cleanedPostcode = activityController.cleanPostcode(input);
        assertEquals("EC1A1BB", cleanedPostcode);
    }

    @Test
    public void testCleanPostcode_WithSpaces() {
        // Test with input containing extra spaces
        String input = "   EC1A 1BB   ";
        String cleanedPostcode = activityController.cleanPostcode(input);
        assertEquals("EC1A1BB", cleanedPostcode);
    }

    @Test
    public void testCleanPostcode_TrimmedInput() {
        // Test with trimmed input
        String input = "EC1A1BB";
        String cleanedPostcode = activityController.cleanPostcode(input);
        assertEquals("EC1A1BB", cleanedPostcode);
    }

    @Test
    public void testCleanPostcode_LongInput() {
        // Test with input longer than 7 characters
        String input = "W1A 1ABCC";
        String cleanedPostcode = activityController.cleanPostcode(input);
        assertEquals("", cleanedPostcode); // Should return empty string
    }

    @Test
    public void testCleanPostcode_EmptyInput() {
        // Test with empty input
        String input = "";
        String cleanedPostcode = activityController.cleanPostcode(input);
        assertEquals("", cleanedPostcode); // Should return empty string
    }


}