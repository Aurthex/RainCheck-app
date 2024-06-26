package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//interface extends CrudRepository which provides basic CRUD operations for Activity entities
public interface ActivityRepository extends CrudRepository<Activity, Integer>{

    // Retrieves an Activity by its ID
    public Optional<Activity> findById(Integer id);

    // Retrieves all activities
    List<Activity> findAll();
}
