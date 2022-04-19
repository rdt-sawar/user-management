package com.example.usermgmt.service;

import java.util.Map;

import com.example.usermgmt.entity.User;
import com.example.usermgmt.pojo.LoginDetails;
import com.example.usermgmt.pojo.RegistrationDetails;
import com.example.usermgmt.pojo.UnlockDetails;

public interface UserMgmtService {

	// login screen
	public User authoriseUser(LoginDetails loginDetails);

	// reg screen
	public String validateEmail(String emailId);

	public Map<Integer, String> getCountries();

	public Map<Integer, String> getStatesForCountry(Integer countryId);

	public Map<Integer, String> getCitiesForState(Integer stateId);

	public RegistrationDetails registerUser(RegistrationDetails userDetails);

	// unlock screen
	public String unlockUserAccount(UnlockDetails unlockDetails);

	// forgot password screen
	public String forgotPassword(String emailId);

	boolean isEmailExist(String email);

}
