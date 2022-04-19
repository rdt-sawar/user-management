package com.example.usermgmt.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.usermgmt.entity.Country;
import com.example.usermgmt.entity.User;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

}
