package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.Condition;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConditionRepository extends CrudRepository<Condition, Integer>{
    public Optional<Condition> findById(Integer id);
    Condition findByWeatherCode(Integer code);
}
