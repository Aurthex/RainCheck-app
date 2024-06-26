package com.raincheck.RainCheck.model;

import jakarta.persistence.*;
import lombok.*;

// Specifies that this class is an entity and will be mapped to a database table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACTIVITY_CONDITION_JOIN")
public class ActivityCondition {

    // Specifies the primary key of the entity
    @Id
    // Specifies how the primary key should be generated
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "condition_id")
    private Condition condition;

    // Default constructor for the ActivityCondition object with specified activity and condition.
    public ActivityCondition(Activity activity, Condition condition){
        this.activity = activity;
        this.condition = condition;
    }
}