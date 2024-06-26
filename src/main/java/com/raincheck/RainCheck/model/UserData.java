package com.raincheck.RainCheck.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

// Specifies that this class is an entity and will be mapped to a database table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserData {

    // Specifies the primary key of the entity
    @Id
    // Specifies how the primary key should be generated
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    @Column
    private Date date;

    @Column
    private String location;
}
