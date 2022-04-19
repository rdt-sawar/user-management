package com.example.usermgmt.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermgmt.entity.User;
import com.example.usermgmt.pojo.LoginDetails;
import com.example.usermgmt.pojo.RegistrationDetails;
import com.example.usermgmt.pojo.UnlockDetails;
import com.example.usermgmt.service.UserMgmtService;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	UserMgmtService userMgmtServiceImpl;

	@PostMapping("/registerUser")
	public String registerUser(@RequestBody RegistrationDetails registrationDetails) {

		System.out.println(registrationDetails.toString());
		if (Objects.isNull(registrationDetails.getFname()) || Objects.isNull(registrationDetails.getLname())
				|| Objects.isNull(registrationDetails.getDob()) || Objects.isNull(registrationDetails.getGender())
				|| Objects.isNull(registrationDetails.getPhone()) || Objects.isNull(registrationDetails.getEmail())) {
			return "Registration failed...Required fields : fname, laname, phone, email, dob, gender, cityId";
		}

		boolean isEmailAlreadyExist = userMgmtServiceImpl.isEmailExist(registrationDetails.getEmail());
		if (isEmailAlreadyExist) {
			return "Registration failed...User with this email already exists";
		}

		RegistrationDetails registeredUser = userMgmtServiceImpl.registerUser(registrationDetails);
		if (registeredUser != null) {
			return "Registration successfull...Please check your registered email for unlock account link ";
		} else {
			return "Registration failed...";
		}

	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDetails loginDetails) {

		System.out.println(loginDetails.toString());
		if (Objects.isNull(loginDetails.getEmail()) || Objects.isNull(loginDetails.getPassword())) {
			return new ResponseEntity<String>("Login failed...Required fields : email , password",
					HttpStatus.BAD_REQUEST);
		}

		User userDetails = userMgmtServiceImpl.authoriseUser(loginDetails);
		if (Objects.isNull(userDetails)) {
			return new ResponseEntity<String>("Login failed...Unauthorised user", HttpStatus.UNAUTHORIZED);

		} else if (!userDetails.isUnlocked()) {
			return new ResponseEntity<String>(
					"user is already registered and Locked... Please check your registered email for unlock account link "
							+ userDetails.getUserId(),
					HttpStatus.LOCKED);

		} else {
			return new ResponseEntity<String>("Login successfull...! Eamil : " + userDetails.getEmail(), HttpStatus.OK);

		}
	}

	@PostMapping("/unlockAccount")
	public ResponseEntity<String> unlockAccount(@RequestBody UnlockDetails unlockDetails) {

		String unlockResult = userMgmtServiceImpl.unlockUserAccount(unlockDetails);
		return new ResponseEntity<String>(unlockResult, HttpStatus.OK);

	}

	@GetMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {

		String forgotPasswordResult = userMgmtServiceImpl.forgotPassword(email);
		return new ResponseEntity<String>(forgotPasswordResult, HttpStatus.OK);

	}

}
