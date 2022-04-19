package com.example.usermgmt.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usermgmt.email.EmailServiceImpl;
import com.example.usermgmt.email.pojo.Mail;
import com.example.usermgmt.entity.City;
import com.example.usermgmt.entity.Country;
import com.example.usermgmt.entity.State;
import com.example.usermgmt.entity.User;
import com.example.usermgmt.pojo.LoginDetails;
import com.example.usermgmt.pojo.RegistrationDetails;
import com.example.usermgmt.pojo.UnlockDetails;
import com.example.usermgmt.repo.CityRepository;
import com.example.usermgmt.repo.CountryRepository;
import com.example.usermgmt.repo.StateRepository;
import com.example.usermgmt.repo.UserRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	StateRepository stateRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	CityRepository cityRepository;

	@Autowired
	EmailServiceImpl emailServiceImpl;

	@Override
	public User authoriseUser(LoginDetails loginDetails) {

		User userDetails = userRepository.findUserByEmailAndPassword(loginDetails.getEmail(),
				loginDetails.getPassword());

		System.out.println(userDetails);
		if (Objects.isNull(userDetails)) {
			return null;
		}
		return userDetails;
	}

	@Override
	public String validateEmail(String emailId) {
		return null;
	}

	@Override
	public Map<Integer, String> getCountries() {

		Iterable<Country> countries = countryRepository.findAll();
		Map<Integer, String> cityMap = new HashMap<>();
		countries.forEach(country -> {
			cityMap.put(country.getCountryId(), country.getCountryName());
		});
		return cityMap;

	}

	@Override
	public Map<Integer, String> getStatesForCountry(Integer countryId) {

		List<State> states = stateRepository.findStateByCountryId(countryId);
		Map<Integer, String> cityMap = new HashMap<>();
		states.forEach(state -> {
			cityMap.put(state.getStateId(), state.getStateName());
		});
		return cityMap;
	}

	@Override
	public Map<Integer, String> getCitiesForState(Integer stateId) {

		List<City> cities = cityRepository.findCityByStateId(stateId);

		Map<Integer, String> cityMap = new HashMap<>();
		cities.forEach(city -> {
			cityMap.put(city.getCityId(), city.getCityName());
		});
		return cityMap;
	}

	@Override
	public RegistrationDetails registerUser(RegistrationDetails userDetails) {

		User user = new User();

		// copu property using bean copy
		// BeanUtils.copyProperties(userDetails, user);

		user.setFirstName(userDetails.getFname());
		user.setLastName(userDetails.getLname());
		user.setEmail(userDetails.getEmail());
		user.setPhoneNumber(userDetails.getPhone());
		user.setDob(userDetails.getDob());
		user.setGender(userDetails.getGender());
		user.setCityId(userDetails.getCityId());

		String tempPass = generateRandomPassword(6);
		user.setPassword(tempPass);
		userDetails.setPassword(tempPass);

		User savedUser = userRepository.save(user);

		// send email - with temp password
		sendRegisrationMail(userDetails);

		return userDetails;
	}

	private void sendRegisrationMail(RegistrationDetails userDetails) {
		String mailBody = readUnlockAccountEmailBody(userDetails);
		Mail mail = new Mail();

		mail.setMailFrom("rdt.sawar@gmail.com");
		mail.setMailTo("rdt.sawar@gmail.com");
		mail.setMailSubject("Regisration Successfull..! User Management..!!");
		mail.setMailContent(mailBody);

		mail.setContentType("text/html; charset=utf-8");
		emailServiceImpl.sendEmail(mail);
	}

	String readUnlockAccountEmailBody(RegistrationDetails userDetails) {
		String body = "";
		StringBuffer buffer = new StringBuffer();

		Path fulePath = Paths.get("email-template-user-registration-success.txt");

		try {
			Stream<String> stream = Files.lines(fulePath);

			stream.forEach(line -> {
				buffer.append(line);
			});

			body = buffer.toString();
			body = body.replace("{FNAME}", userDetails.getFname());
			body = body.replace("{LNAME}", userDetails.getLname());
			body = body.replace("{TEMP-PWD}", userDetails.getPassword());
			body = body.replace("{EMAIL}", userDetails.getEmail());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	String readForgotPasswordEmailBody(User userDetails) {
		String body = "";
		StringBuffer buffer = new StringBuffer();

		Path fulePath = Paths.get("email-template-forgot-pass.txt");

		try {
			Stream<String> stream = Files.lines(fulePath);

			stream.forEach(line -> {
				buffer.append(line);
			});

			body = buffer.toString();
			body = body.replace("{FNAME}", userDetails.getFirstName());
			body = body.replace("{LNAME}", userDetails.getLastName());
			body = body.replace("{PWD}", userDetails.getPassword());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	// function to generate a random string of length n
	static String generateRandomPassword(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	@Override
	public boolean isEmailExist(String email) {

		User userDetails = userRepository.findUserByEmail(email);
		if (Objects.isNull(userDetails)) {
			return false;
		}
		return true;
	}

	@Override
	public String unlockUserAccount(UnlockDetails unlockDetails) {

		User userDetails = userRepository.findUserByEmailAndPassword(unlockDetails.getEmail(),
				unlockDetails.getTempPassword());

		if (userDetails == null) {
			return "invalid password..";
		} else {

			userDetails.setPassword(unlockDetails.getNewPassword());
			userDetails.setUnlocked(true);
			int unlockResult = userRepository.unlockUser(unlockDetails.getEmail());

			System.out.println(unlockResult);
			return "SUCCESS...User Account unlocked";

		}

	}

	@Override
	public String forgotPassword(String emailId) {

		User userDetails = userRepository.findUserByEmail(emailId);
		if (userDetails == null) {
			return "invalid email..";
		} else {
			sendForgotPasswordMail(userDetails);
			return "password sent to registered email please check";
		}

	}

	private void sendForgotPasswordMail(User userDetails) {
		String mailBody = readForgotPasswordEmailBody(userDetails);
		Mail mail = new Mail();

		mail.setMailFrom("rdt.sawar@gmail.com");
		mail.setMailTo("rdt.sawar@gmail.com");
		mail.setMailSubject("Forgot Password..! User Management..!!");
		mail.setMailContent(mailBody);

		mail.setContentType("text/html; charset=utf-8");
		emailServiceImpl.sendEmail(mail);
	}

}
