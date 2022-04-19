package com.example.usermgmt.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.usermgmt.entity.State;
import com.example.usermgmt.entity.User;
@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

	
	java.util.List<State> findStateByCountryId(Integer countryId);
}
