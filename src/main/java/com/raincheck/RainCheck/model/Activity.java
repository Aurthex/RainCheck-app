package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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
    @Column
    private String location;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @Column(nullable = true)
    private Date date;
    @Column(name="score")
    private Integer dateScore;
    @Transient private Condition[] conditions;
    @Transient private Integer score;
    @Transient private String scoreMessage;

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


        return "{" +
                "\"id\":\"" + displayInteger(id) + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"temperature\":\"" + displayInteger(temperature) + "\"," +
                "\"windSpeed\":\"" + displayInteger(windSpeed) + "\"," +
                "\"score\":\"" + displayInteger(score) + "\"," +
                "\"scoreMessage\":\"" + scoreMessage + "\"," +
                "\"conditions\":" + conditionsJson.toString() +
                "}";
    }

    public String displayInteger(Integer i){
        if (i != null) return i.toString();
        return "any";
    }

    public void generateWeatherComparisons(Weather weather){
        int max = 0;
        int score = 0;
        if (conditions.length > 0){
            max += 15;
            score += getCodeStatement(weather);
        }
        if (temperature != null){
            max += 10;
            score += getTempStatement(weather);
        }
        if (windSpeed != null){
            max += 5;
            score += getSpeedStatement(weather);
        }

        if (max == 0) score = 100;
        else{
            double ratio = ((double)score / (double)max) * 100;
            score = (int) Math.round(ratio);
        }
        this.score = score;
        this.scoreMessage = getScoreMessage(score);
    }

    String getScoreMessage(Integer score){
        if (score >= 95) return "The weather is a perfect match!";
        if (score >= 85) return "The weather is ideal for your activity.";
        if (score >= 75) return "The weather is close to what you want.";
        if (score >= 50) return "You could maybe make this work.";
        return "Maybe you should do something else.";
    }

    private Integer getCodeStatement(Weather weather){
        if (conditions.length == 0) return 0;
        int min = 3;
        for (Condition condition : conditions){
            int conditionCode = condition.getWeatherCode();
            int weatherCode = weather.getWeather_code();
            int dif = Math.abs(conditionCode - weatherCode);
            min = Math.min(dif,min);
        }
        int penalty = min * 5;
        return 15 - penalty;
    }

    Integer getTempStatement(Weather weather){
        if (temperature == null) return 0;
        int dif = Math.abs(weather.getTemperature() - temperature);

        dif = Math.min(dif, 10);
        return 10 - dif;
    }

    Integer getSpeedStatement(Weather weather){
        if (windSpeed == null) return 0;
        int dif = Math.abs(weather.getWind_speed() - windSpeed);
        dif = Math.min(dif, 20);
        return 5 - (dif/4);
    }

    public String getConditionString(){
        if (conditions.length == 0) return "Any Weather";
        StringBuilder conditionString = new StringBuilder();
        for (int i = 0; i < conditions.length-1; i++){
            conditionString.append((conditions[i].getName()));
            conditionString.append(" - ");
        }
        conditionString.append((conditions[conditions.length-1].getName()));
        return conditionString.toString();
    }

    public void resetBooking(){
        date = null;
        dateScore = null;
        location = null;
        latitude = null;
        longitude = null;
    }
}
