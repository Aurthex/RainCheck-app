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
                "\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"temperature\":\"" + displayInteger(temperature) + "\"," +
                "\"windSpeed\":\"" + displayInteger(windSpeed) + "\"," +
                "\"conditions\":" + conditionsJson.toString() +
                "}";
    }

    public String displayInteger(Integer i){
        if (i != null) return i.toString();
        return "any";
    }
}
