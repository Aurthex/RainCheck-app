package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

// Specifies that this class is an entity and will be mapped to a database table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONDITIONS")
public class Condition {

    // Specifies the primary key of the entity
    @Id
    // Specifies how the primary key should be generated
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer weatherCode;
    @Column
    private String weatherIcon;

    // Default constructor for the Condition class.
    public Condition(int id, String name, int weatherCode)
    {
        this.id = id;
        this.name = name;
        this.weatherCode = weatherCode;
    }
}