package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

//@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONDITIONS")
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer weatherCode;
    @Column
    private String weatherIcon;
}