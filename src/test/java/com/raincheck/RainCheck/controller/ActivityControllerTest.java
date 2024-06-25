package com.raincheck.RainCheck.controller;

import com.raincheck.RainCheck.client.PostCodeClient;
import com.raincheck.RainCheck.client.WeatherClient;
import com.raincheck.RainCheck.model.*;
import com.raincheck.RainCheck.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
    private UserDataRepository userDataRepository;

    @InjectMocks
    private ActivityController activityController;


    private Activity activity;
    private Condition condition;
    private Condition condition2;
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
        condition2 = new Condition(2, "Rainy", 200, "rain.png");
        activityCondition = new ActivityCondition(activity, condition);

        weather = new Weather();
        weather.setWeather_code(100);

        userData = new UserData();
        userData.setId(1);
        userData.setDate(Date.valueOf(LocalDate.now()));
        userData.setLatitude("52.5200");
        userData.setLongitude("13.4050");
    }



//    @Test
//    public void testIndex() throws Exception {
//
//    }

    /**
     * Test method for showing activities.
     * Mocks repository calls to return predefined activity and activity conditions.
     * Verifies that the model attribute "activities" is set correctly.
     */
    @Test
    public void testShowActivities() {
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
        when(conditionRepository.findAll()).thenReturn(Collections.singletonList(condition));

        String viewName = activityController.addNewActivity(model);

        verify(model, times(1)).addAttribute(eq("activity"), any(Activity.class));
        verify(model, times(1)).addAttribute("conditions", Collections.singletonList(condition));
        assertEquals("activities/new", viewName);
    }

    /**
     * Test method for getting activities with conditions.
     * Mocks repository calls to return predefined activity and activity conditions.
     * Verifies that the retrieved activities contain the expected conditions.
     */
    @Test
    public void testGetActivitiesWithConditions() {
        when(activityRepository.findAll()).thenReturn(Collections.singletonList(activity));
        when(activityConditionRepository.findByActivity(any(Activity.class))).thenReturn(Collections.singletonList(activityCondition));

        List<Activity> activities = activityController.getActivitiesWithConditions();

        assertEquals(1, activities.size());
        assertEquals(1, activities.get(0).getConditions().length);
        assertEquals("Sunny", activities.get(0).getConditions()[0].getName());
    }

    @Test
    public void testDeleteActivity(){
        Integer activityId = 1;
        RedirectView redirectView = activityController.deleteActivity(activityId);
        verify(activityRepository, times(1)).deleteById(activityId);
        assertEquals("/activities", redirectView.getUrl());
    }
    @Test
    public void testEditActivity() {
        // Mock dependencies
        when(activityRepository.findById(anyInt())).thenReturn(Optional.of(activity));
        when(conditionRepository.findAll()).thenReturn(Collections.singletonList(condition));

        // Call the editActivity method under test
        String viewName = activityController.editActivity(1, model);

        // Verify that the "activities/new" view name is returned
        assertEquals("activities/new", viewName);

        // Verify that the model.addAttribute method was called with expected attributes
        verify(model, times(1)).addAttribute(eq("activity"), any(Activity.class));
        verify(model, times(1)).addAttribute(eq("conditions"), anyList());
    }
    @Test
    public void testGetWeatherIcon() throws Exception {

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
        activity = new Activity();
        activity.setId(1);
        activity.setName("Running");
        activity.setDescription("Outdoor running");

        condition = new Condition();
        condition.setId(1);
        condition.setName("Sunny");
        condition.setWeatherCode(100);
        condition.setWeatherIcon("sun.png");

        condition2 = new Condition();
        condition2.setId(2);
        condition2.setName("Rainy");
        condition2.setWeatherCode(200);
        condition2.setWeatherIcon("rain.png");

        activity.setConditions(new Condition[]{condition, condition2});

        // Mock activity repository save method
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        // Mock activity condition repository methods
        when(activityConditionRepository.findByActivity(any(Activity.class))).thenReturn(Collections.emptyList());

        // Call the method under test
        RedirectView redirectView = activityController.addNewActivity(activity);

        // Verify that the activity was saved
        verify(activityRepository, times(1)).save(activity);

        // Verify that the activity conditions were retrieved and deleted
        verify(activityConditionRepository, times(1)).findByActivity(activity);
        verify(activityConditionRepository, times(1)).deleteAll(anyList());

        // Verify that new activity conditions were saved
        verify(activityConditionRepository, times(2)).save(any(ActivityCondition.class));

        // Verify the redirect URL
        assertEquals("/activities", redirectView.getUrl());
    }
}