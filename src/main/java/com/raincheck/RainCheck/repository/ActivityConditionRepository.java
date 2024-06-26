package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.Activity;
import com.raincheck.RainCheck.model.ActivityCondition;
import com.raincheck.RainCheck.model.Condition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//interface extends CrudRepository which provides basic CRUD operations for ActivityCondition entities
public interface ActivityConditionRepository extends CrudRepository<ActivityCondition, Integer>{

    // Retrieves an ActivityCondition by its ID
    public Optional<ActivityCondition> findById(Integer id);

    // Retrieves all ActivityConditions associated with a specific Activity
    public List<ActivityCondition> findByActivity(Activity activity);

    // Retrieves all ActivityConditions associated with a specific Condition
    public List<ActivityCondition> findByCondition(Condition condition);

    // Retrieves all Conditions associated with a specific Activity
    public List<Condition> findConditionByActivity(Activity activity);


//    public List<ActivityCondition> GetByTemperatureGreaterThanLowAndTemperatureLessThanHigh(Integer low, Integer high);

}
