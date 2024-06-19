package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.Activity;
import com.raincheck.RainCheck.model.ActivityCondition;
import com.raincheck.RainCheck.model.Condition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityConditionRepository extends CrudRepository<ActivityCondition, Integer>{
    public Optional<ActivityCondition> findById(Integer id);
    public List<ActivityCondition> findByActivity(Activity activity);
    public List<ActivityCondition> findByCondition(Condition condition);

//    public List<ActivityCondition> GetByTemperatureGreaterThanLowAndTemperatureLessThanHigh(Integer low, Integer high);

}
