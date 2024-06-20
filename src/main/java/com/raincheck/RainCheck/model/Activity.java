package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public String toJson() {
        return "{" +
                "\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\"" +
                // Add other properties in JSON format
                "}";
    }
}
