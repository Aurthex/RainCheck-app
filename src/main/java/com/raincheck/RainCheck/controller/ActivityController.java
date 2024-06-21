package com.raincheck.RainCheck.controller;

import com.raincheck.RainCheck.client.WeatherClient;
import com.raincheck.RainCheck.model.Activity;
import com.raincheck.RainCheck.model.ActivityCondition;
import com.raincheck.RainCheck.model.Condition;
import com.raincheck.RainCheck.model.Weather;
import com.raincheck.RainCheck.repository.ActivityConditionRepository;
import com.raincheck.RainCheck.repository.ActivityRepository;
import com.raincheck.RainCheck.repository.ConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityConditionRepository activityConditionRepository;

    @Autowired
    ConditionRepository conditionRepository;

    @GetMapping("/")
    public String index(Model model) throws IOException, InterruptedException {

        //API request to get weather data as a JSON
        WeatherClient client = new WeatherClient("54.9783", "-1.6178");
        String current = client.findCurrent();

        //Pass current weather JSON into weather and instantiate and add to model
        Weather weather = new Weather(current); // new Weather(current, forecast)
        model.addAttribute("weather", weather);

        String weather_icon = conditionRepository.findByWeatherCode(weather.getWeather_code()).getWeatherIcon();
        if (weather_icon == null) weather_icon = "";
        model.addAttribute("weather_icon", weather_icon);

        //Get all activities with conditions and add to model
        List<Activity> activities = GetActivitiesWithConditions();
        model.addAttribute("activities", activities);

        return "index";
    }

    @RequestMapping(value = "/activities")
    public String showActivities(Model model) {
        //Get all activities with conditions and add to model
        List<Activity> activities = GetActivitiesWithConditions();
        model.addAttribute("activities", activities);
        return "/activities";
    }

    @PostMapping("/activities")
    public RedirectView create(@ModelAttribute Activity activity, @RequestParam("weather_codes") List<Integer> weather_codes){
        activityRepository.save(activity);

        for (Integer code : weather_codes){
            Condition condition = conditionRepository.findByWeatherCode(code);
            ActivityCondition activityCondition = new ActivityCondition(activity, condition);
            activityConditionRepository.save(activityCondition);
        }

        return new RedirectView("/activities");
    }

    //Creates a list of all activities and then populates each activity objects "conditions"
    //Field with all conditions associated with it through the join table
    public List<Activity> GetActivitiesWithConditions(){
        List<Activity> activities = activityRepository.findAll(); //Get all activities in table
        for (Activity activity: activities){
            //For each activity, get all activityconditions associated through the join table
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
        return activities;
    }

    @GetMapping("/activities/new")
    public String addNewActivity(Model model) {
        model.addAttribute("activity", new Activity());
        Iterable<Condition> conditions = conditionRepository.findAll();
        model.addAttribute("conditions", conditions);
        return "activities/new";
    }

    @PostMapping("/activities/new")
    public RedirectView addNewActivity(@ModelAttribute Activity activity) {
        activityRepository.save(activity);
        return new RedirectView("/activities");
    }


}
