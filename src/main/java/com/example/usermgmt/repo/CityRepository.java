package com.example.usermgmt.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.usermgmt.entity.City;
import com.example.usermgmt.entity.User;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {
	
	List<City> findCityByStateId(Integer stateId);

}
