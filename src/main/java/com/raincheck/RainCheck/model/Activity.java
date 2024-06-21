package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ACTIVITIES")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column
    private Integer temperature;
    @Column
    private Integer windSpeed;
    @Transient private Condition[] conditions;
    @Transient private String weatherComparisons;

    public Activity(Integer id, String name, String description, Condition[] conditions){
        this.id = id;
        this.name = name;
        this.description = description;
        this.conditions = conditions;
    }

    public String toJson() {
        StringBuilder conditionsJson = new StringBuilder("[");
        for (int i = 0; i < conditions.length; i++) {
            conditionsJson.append("{\"name\":\"").append(conditions[i].getName()).append("\"}");
            if (i < conditions.length - 1) {
                conditionsJson.append(",");
            }
        }
        conditionsJson.append("]");

        System.out.println(conditionsJson);

        return "{" +
                "\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"temperature\":\"" + displayInteger(temperature) + "\"," +
                "\"windSpeed\":\"" + displayInteger(windSpeed) + "\"," +
                "\"weatherComparisons\":\"" + weatherComparisons + "\"," +
                "\"conditions\":" + conditionsJson.toString() +
                "}";
    }

    public String displayInteger(Integer i){
        if (i != null) return i.toString();
        return "any";
    }

    public void generateWeatherComparisons(Weather weather){
        int count = 0;
        String codeStatement = "";
        String tempStatement = "";
        String speedStatement = "";
        if (conditions.length > 0){
            count++;
            codeStatement = getCodeStatement(weather);
        }
        if (temperature != null){
            count ++;
            tempStatement = getTempStatement(weather);
        }
        if (windSpeed != null){
            count ++;
            speedStatement = getSpeedStatement(weather);
        }

        if (count == 0) weatherComparisons = "This activity has no conditions.";
        else{
            String[] comparisons = new String[count];
            count--;
            if (!speedStatement.isEmpty()){
                comparisons[count] = speedStatement;
                count--;
            }
            if (!tempStatement.isEmpty()){
                comparisons[count] = tempStatement;
                count--;
            }
            if (!codeStatement.isEmpty()){
                comparisons[count] = codeStatement;
            }
            weatherComparisons = String.join("<br>", comparisons);
        }
    }

    private String getCodeStatement(Weather weather){
        if (conditions.length == 0) return "";
        for (Condition condition : conditions){
            if (condition.getWeatherCode() == weather.getWeather_code()) return "The weather matches your conditions!";
        }
        return "The weather does not match your conditions.";
    }

    private String getTempStatement(Weather weather){
        if (temperature == null) return "";
        if (weather.getTemperature() - 8 >= temperature) return "It is a lot hotter than you'd like.";
        if (weather.getTemperature() - 3 >= temperature) return "It is a little hotter than you'd like.";
        if (Math.abs(weather.getTemperature() - temperature) < 3) return "The temperature is about right!";
        if (weather.getTemperature() - 3 <= temperature) return "It is a little colder than you'd like.";
        if (weather.getTemperature() - 8 <= temperature) return "It is a lot colder than you'd like.";
        return "You should never see this string.";
    }

    private String getSpeedStatement(Weather weather){
        if (windSpeed == null) return "";
        if (weather.getWind_speed() - 10 >= windSpeed) return "It is a lot more windy than you'd like.";
        if (weather.getWind_speed() - 5 >= windSpeed) return "It is a little more windy than you'd like.";
        if (Math.abs(weather.getWind_speed() - windSpeed) < 3) return "The wind speed is about right!";
        if (weather.getWind_speed() - 10 <= windSpeed) return "It is a lot less windy than you'd like.";
        if (weather.getWind_speed() - 5 <= windSpeed) return "It is a little less windy than you'd like.";

        return "You should never see this string.";
    }
}
