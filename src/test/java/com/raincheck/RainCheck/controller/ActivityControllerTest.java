package com.raincheck.RainCheck.controller;

import com.raincheck.RainCheck.model.Weather;

import com.raincheck.RainCheck.model.Activity;
import com.raincheck.RainCheck.model.ActivityCondition;
import com.raincheck.RainCheck.model.Condition;
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
import java.util.Collections;
import java.util.List;

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

    @InjectMocks
    private ActivityController activityController;

    private Activity activity;
    private Condition condition;
    private ActivityCondition activityCondition;

    /**
     * Setup method to initialize mock objects and test data before each test method.
     */
    @BeforeEach
    public void setUp() {
        activity = new Activity(1, "Running", "Outdoor running", null);
        condition = new Condition(1, "Sunny", 100, "sun.png");
        activityCondition = new ActivityCondition(activity, condition);
    }
    

    /**
     * Test method for showing activities.
     * Mocks repository calls to return predefined activity and activity conditions.
     * Verifies that the model attribute "activities" is set correctly.
     */
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
}