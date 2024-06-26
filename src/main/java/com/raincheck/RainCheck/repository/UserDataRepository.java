package com.raincheck.RainCheck.repository;
import com.raincheck.RainCheck.model.UserData;
import org.springframework.data.repository.CrudRepository;

// interface extends CrudRepository which provides basic CRUD operations for UserData entities
public interface UserDataRepository extends CrudRepository<UserData, Integer>{
    // No additional methods defined
}
