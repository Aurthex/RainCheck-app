package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.Condition;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// interface extends CrudRepository which provides basic CRUD operations for Condition entities
public interface ConditionRepository extends CrudRepository<Condition, Integer>{

    // Retrieves a Condition by its ID
    public Optional<Condition> findById(Integer id);

    // Retrieves a Condition by its weather code
    Condition findByWeatherCode(Integer code);
}
