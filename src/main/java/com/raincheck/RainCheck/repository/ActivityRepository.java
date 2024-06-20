package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends CrudRepository<Activity, Integer>{
    public Optional<Activity> findById(Integer id);
    List<Activity> findAll();
}
