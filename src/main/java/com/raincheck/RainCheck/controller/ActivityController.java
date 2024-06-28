package com.raincheck.RainCheck.controller;

import com.raincheck.RainCheck.client.PostCodeClient;
import com.raincheck.RainCheck.client.WeatherClient;
import com.raincheck.RainCheck.model.*;
import com.raincheck.RainCheck.repository.ActivityConditionRepository;
import com.raincheck.RainCheck.repository.ActivityRepository;
import com.raincheck.RainCheck.repository.ConditionRepository;
import com.raincheck.RainCheck.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ActivityController {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityConditionRepository activityConditionRepository;

    @Autowired
    ConditionRepository conditionRepository;

    @Autowired
    UserDataRepository userDataRepository;

    // MAPPING METHODS

/**
 * Handles requests to the root URL ("/").
 * Retrieves user data, weather information, activities with conditions, and formats dates for display.
 **/
    @GetMapping("/")
    public String index(Model model) throws IOException, InterruptedException
    {
        UserData userData = getUserData();
        model.addAttribute("userData", userData);

        Weather weather = getWeather(userData);
        model.addAttribute("weather", weather);

        String description = conditionRepository.findByWeatherCode(weather.getWeather_code()).getName();
        model.addAttribute("weather_description", description);

        String weather_icon = getWeatherIcon(weather);
        model.addAttribute("weather_icon", weather_icon);

        //Get all activities with conditions and add to model
        List<Activity> activities = getActivitiesWithConditions(weather);

        // Sort activities alphabetically by name
        activities.sort(Comparator.comparing(Activity::getName));
        model.addAttribute("activities", activities);

        // Get today and tomorrow's date and format them to YYYY-MM-DD format
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedToday = today.format(formatter);
        String formattedTomorrow = tomorrow.format(formatter);

        model.addAttribute("today", formattedToday);
        model.addAttribute("tomorrow", formattedTomorrow);

        return "index";
    }

/**
 * Handles POST requests to update user settings.
 * Updates user data based on provided postcode and redirects to the root URL.
 **/
    @PostMapping("/update_settings")
    public RedirectView updateUserData(@ModelAttribute UserData userData, @RequestParam String postcode) throws URISyntaxException, IOException, InterruptedException {
        //Clean input
        postcode = cleanPostcode(postcode);
        //Pass postcode to client and generate internal values
        PostCodeClient client = new PostCodeClient(postcode);

        //If client fails to generate valid values, use existing values from userdata
        if (client.getLatitude() != null){
            userData.setLatitude(client.getLatitude());
            userData.setLongitude(client.getLongitude());
            userData.setLocation(client.getLocation());
        }

        userDataRepository.save(userData);
        return new RedirectView("/");
    }

    /**
     * Displays a list of all activities.
     * Retrieves activities with conditions and sorts them alphabetically by name.
     **/
    @GetMapping(value = "/activities")
    public String showActivities(Model model) throws IOException, InterruptedException {
        //Get all activities with conditions and add to model
        List<Activity> activities = getActivitiesWithConditions(null);

        // Sort activities alphabetically by name
        activities.sort(Comparator.comparing(Activity::getName));
        model.addAttribute("activities", activities);
        return "activities/activities";
    }

/**
 * Displays the form to add a new activity.
 * Retrieves all conditions and adds them to the model.
 **/
    @GetMapping("/activities/new")
    public String addNewActivity(Model model) {
        model.addAttribute("activity", new Activity());
        Iterable<Condition> conditions = conditionRepository.findAll();
        model.addAttribute("conditions", conditions);
        return "activities/new";
    }

/**
 * Displays the form to edit an existing activity.
 * Retrieves the activity by ID, populates its conditions, retrieves all conditions, and adds them to the model.
 **/
    @GetMapping("/activities/edit")
    public String editActivity(@RequestParam("activityId") int id, Model model) {
        Optional<Activity> activityOp = activityRepository.findById(id);
        if (activityOp.isEmpty()) return "error";
        Activity activity = activityOp.get();
        populateActivityConditions(activity);
        model.addAttribute("activity", activity);

        Iterable<Condition> conditions = conditionRepository.findAll();
        model.addAttribute("conditions", conditions);
        return "activities/new";
    }

/**
 * Handles POST requests to add a new activity.
 * Saves the activity and its associated conditions, then redirects to the activities page.
 **/
    @PostMapping("/activities")
    public RedirectView addNewActivity(@ModelAttribute Activity activity) {
        if (activity.getLocation() == null || activity.getLocation().isEmpty()) activity.resetBooking();

        activityRepository.save(activity);

        var activityConditions = activityConditionRepository.findByActivity(activity);

        activityConditionRepository.deleteAll(activityConditions);

        for (Condition condition : activity.getConditions()){
            ActivityCondition activityCondition = new ActivityCondition(activity, condition);
            activityConditionRepository.save(activityCondition);
        }
        return new RedirectView("/activities");
    }

/**
 * Handles POST requests to delete an activity.
 * Deletes the activity by ID and redirects to the activities page.
 **/
    @PostMapping("/activities/delete/{id}")
    public RedirectView deleteActivity(@PathVariable Integer id) {
        activityRepository.deleteById(id);
        return new RedirectView("/activities");
    }

/**
 * Handles POST requests to remove booking details from an activity.
 * Resets booking details (date, location, latitude, longitude) for the activity by ID and redirects to the activities page.
 **/
    @PostMapping("/remove_booking/{id}")
    public RedirectView removeBooking(@PathVariable Integer id) {
        Optional<Activity> activityOptional = activityRepository.findById(id);
        if (activityOptional.isEmpty()) return new RedirectView("/activities");
        Activity activity = activityOptional.get();
        activity.resetBooking();
        activityRepository.save(activity);
        return new RedirectView("/activities");
    }

/**
 * Handles POST requests to book an activity.
 * Retrieves the activity by ID, retrieves user data, retrieves weather information,
 * populates activity conditions, generates weather comparisons, saves booking details,
 * and redirects to the root URL ("/").
 **/
    @PostMapping("/book_activity")
    public RedirectView bookActivity(@RequestParam("activityId") String id) throws IOException, InterruptedException {
        Optional<Activity> activityOptional = activityRepository.findById(Integer.parseInt(id));
        if (activityOptional.isEmpty()) return new RedirectView("/");

        Activity activity = activityOptional.get();
        UserData userData = getUserData();
        Weather weather = getWeather(userData);
        populateActivityConditions(activity);
        activity.generateWeatherComparisons(weather);

        activity.setLocation(userData.getLocation());
        activity.setLatitude(userData.getLatitude());
        activity.setLongitude(userData.getLongitude());
        activity.setDate(userData.getDate());
        activity.setDateScore(activity.getScore());

        activityRepository.save(activity);
        return new RedirectView("/");
    }

    // UTILITY METHODS

    // Cleans postcode input by removing spaces and quotes.
    String cleanPostcode(String input){
        String postcode = input.replace(" ", "");
        postcode = postcode.replace("\"", "");
        postcode = postcode.trim();
        if (postcode.length() > 7) return "";
        return postcode;
    }

    // Retrieves user data from the database.
    // Updates user's date to current date if necessary and saves updated user data.
    UserData getUserData(){
        UserData userData = userDataRepository.findAll().iterator().next();

        Date date = userData.getDate();
        Date now = Date.valueOf(LocalDate.now());
        if (now.after(date)) date = now;

        userData.setDate(date);
        userDataRepository.save(userData);
        return userData;
    }

    // Retrieves weather data based on user data (latitude, longitude, date).
    Weather getWeather(UserData userData) throws IOException, InterruptedException {
        //API request to get weather data as a JSON
        WeatherClient client = new WeatherClient(userData.getLatitude(), userData.getLongitude(), userData.getDate());
        String daily = client.findDaily();

        //Pass current weather JSON into weather and instantiate and add to model
        return new Weather(daily);
    }

    // Retrieves weather data based on activity's location, latitude, longitude, and date.
    Weather getWeatherFromBooking(Activity activity) throws IOException, InterruptedException {
        //API request to get weather data as a JSON
        WeatherClient client = new WeatherClient(activity.getLatitude(), activity.getLongitude(), activity.getDate());
        String daily = client.findDaily();

        //Pass current weather JSON into weather and instantiate and add to model
        return new Weather(daily);
    }

    // Retrieves weather icon based on weather condition code.
    String getWeatherIcon(Weather weather){
        Integer code = weather.getWeather_code();
        Condition condition = conditionRepository.findByWeatherCode(code);
        String icon = condition.getWeatherIcon();
        if (icon == null || icon.isEmpty()) return "";
        return icon;
    }

    // Retrieves a list of all activities and populates each activity's conditions field.
    public List<Activity> getActivitiesWithConditions(){
        List<Activity> activities = activityRepository.findAll(); //Get all activities in table
        for (Activity activity: activities){
            //For each activity, get all activityconditions associated through the join table
            populateActivityConditions(activity);
        }
        return activities;
    }

    // Retrieves weather data from booking and generates weather comparisons for the activity.
    public void checkBooking(Activity activity) throws IOException, InterruptedException {
        Weather weather = getWeatherFromBooking(activity);
        activity.generateWeatherComparisons(weather);
    }

    // Validates activities by checking their booking dates.
    // Resets booking details for activities that are past their date.
    public void validateActivities(){
        List<Activity> activities = activityRepository.findAll();
        for (Activity activity: activities){
            Date date = activity.getDate();
            if (date == null) continue;
            Date now = Date.valueOf(LocalDate.now());
            if (!now.after(date)) continue;
            activity.resetBooking();
            activityRepository.save(activity);
        }
    }

    // Retrieves a list of all activities and populates each activity's conditions field.
    // Compares weather conditions if weather is provided and validates activities.
    public List<Activity> getActivitiesWithConditions(Weather weather) throws IOException, InterruptedException {
        validateActivities();
        List<Activity> activities = activityRepository.findAll(); //Get all activities in table
        for (Activity activity: activities){
            //For each activity, get all activityconditions associated through the join table
            populateActivityConditions(activity);

            //For each activity, compare weather conditions
            //activity.generateWeatherComparisons(weather);
            if (weather != null) activity.generateWeatherComparisons(weather);
            else if (activity.getLocation() != null) checkBooking(activity);
        }
        return activities;
    }

    // Populates an activity's conditions field from its associated activity conditions.
    public void populateActivityConditions(Activity activity){
        var conditions = new ArrayList<Condition>();
        var activityConditions = activityConditionRepository.findByActivity(activity);

        //iterate over each activity condition and populate arraylist with conditions
        for (ActivityCondition activityCondition: activityConditions){
            conditions.add(activityCondition.getCondition());
        }

        //Convert arraylist to array and assign to activity.conditions
        Condition[] conditionsAr = new Condition[conditions.size()];
        for (int i = 0; i < conditions.size(); i++) conditionsAr[i] = conditions.get(i);
        activity.setConditions(conditionsAr);
    }

}
