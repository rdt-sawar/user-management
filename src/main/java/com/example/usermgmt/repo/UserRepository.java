package com.example.usermgmt.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.usermgmt.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findUserByEmail(String email);
	
	User findUserByEmailAndPassword(String email, String password);
	
	@Transactional
	@Modifying
	@Query("update User u set u.isUnlocked = true where u.email = :emailId")
	int unlockUser(@Param("emailId") String emailId);
	
}
