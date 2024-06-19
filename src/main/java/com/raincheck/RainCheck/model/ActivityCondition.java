package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

//@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACTIVITY_CONDITION_JOIN")
public class ActivityCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @ManyToOne
    @JoinColumn(name = "condition_id")
    private Condition condition;
}