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

    @GetMapping("/")
    public String index(Model model) throws IOException, InterruptedException {
        UserData userData = userDataRepository.findAll().iterator().next();

        Date date = userData.getDate();
        Date now = Date.valueOf(LocalDate.now());
        if (now.after(date)) date = now;

        userData.setDate(date);
        userDataRepository.save(userData);
        model.addAttribute("userData", userData);

        //API request to get weather data as a JSON
        WeatherClient client = new WeatherClient(userData.getLatitude(), userData.getLongitude(), userData.getDate());
        String daily = client.findDaily();

        //Pass current weather JSON into weather and instantiate and add to model
        Weather weather = new Weather(daily); // new Weather(current, forecast)
        model.addAttribute("weather", weather);

        String weather_icon = conditionRepository.findByWeatherCode(weather.getWeather_code()).getWeatherIcon();
        if (weather_icon == null) weather_icon = "";
        model.addAttribute("weather_icon", weather_icon);

        //Get all activities with conditions and add to model
        List<Activity> activities = GetActivitiesWithConditions(weather);

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

    @PostMapping("/update_settings")
    public RedirectView updateUserData(@ModelAttribute UserData userData, @RequestParam String postcode) throws URISyntaxException, IOException, InterruptedException {
        //TODO validate postcode
        postcode = postcode.replace(" ", "");
        postcode = postcode.trim();
        //If postcode is invalid, use existing postcode
        PostCodeClient client = new PostCodeClient(postcode);
        //TODO set Long and Lat from userData postcode before saving

        if (client.getLatitude() != null){
            userData.setLatitude(client.getLatitude());
            userData.setLongitude(client.getLongitude());
            userData.setLocation(client.getLocation());
        }

        userDataRepository.save(userData);
        return new RedirectView("/");
    }

    @GetMapping(value = "/activities")
    public String showActivities(Model model) {
        //Get all activities with conditions and add to model
        List<Activity> activities = GetActivitiesWithConditions();

        // Sort activities alphabetically by name
        activities.sort(Comparator.comparing(Activity::getName));
        model.addAttribute("activities", activities);
        return "activities/activities";
    }

    //Creates a list of all activities and then populates each activity objects "conditions"
    //Field with all conditions associated with it through the join table
    public List<Activity> GetActivitiesWithConditions(){
        List<Activity> activities = activityRepository.findAll(); //Get all activities in table
        for (Activity activity: activities){
            //For each activity, get all activityconditions associated through the join table
            populateActivityConditions(activity);
        }
        return activities;
    }

    public List<Activity> GetActivitiesWithConditions(Weather weather){
        List<Activity> activities = activityRepository.findAll(); //Get all activities in table
        for (Activity activity: activities){
            //For each activity, get all activityconditions associated through the join table
            populateActivityConditions(activity);

            //For each activity, compare weather conditions
            activity.generateWeatherComparisons(weather);
        }
        return activities;
    }

    public void populateActivityConditions(Activity activity){
        var conditions = new ArrayList<Condition>();
        var activityConditions = activityConditionRepository.findByActivity(activity);

        //iterate over each activitycondition and populate arraylist with conditions
        for (ActivityCondition activityCondition: activityConditions){
            conditions.add(activityCondition.getCondition());
        }

        //Convert arraylist to array and assign to activity.conditions
        Condition[] conditionsAr = new Condition[conditions.size()];
        for (int i = 0; i < conditions.size(); i++) conditionsAr[i] = conditions.get(i);
        activity.setConditions(conditionsAr);
    }

    //Get the page to add a new activity

    @GetMapping("/activities/new")
    public String addNewActivity(Model model) {
        model.addAttribute("activity", new Activity());
        Iterable<Condition> conditions = conditionRepository.findAll();
        model.addAttribute("conditions", conditions);
        return "activities/new";
    }

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

    @PostMapping("/activities")
    public RedirectView addNewActivity(@ModelAttribute Activity activity) {
        activityRepository.save(activity);

        var activityConditions = activityConditionRepository.findByActivity(activity);

        activityConditionRepository.deleteAll(activityConditions);

        for (Condition condition : activity.getConditions()){
            ActivityCondition activityCondition = new ActivityCondition(activity, condition);
            activityConditionRepository.save(activityCondition);
        }
        return new RedirectView("/activities");
    }

    // Delete an activity on the activity page

    @PostMapping("/activities/delete/{id}")
    public RedirectView deleteActivity(@PathVariable Integer id) {
        activityRepository.deleteById(id);
        return new RedirectView("/activities");
    }


}
