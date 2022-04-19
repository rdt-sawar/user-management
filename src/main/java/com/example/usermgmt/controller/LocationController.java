package com.example.usermgmt.controller;

import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermgmt.service.UserMgmtService;

@RestController
@CrossOrigin
public class LocationController {

	@Autowired
	UserMgmtService userMgmtService;

	@GetMapping("/countries")
	public ResponseEntity<Map<Integer, String>> getCountries() {
		Map<Integer, String> countries = userMgmtService.getCountries();
		return new ResponseEntity<>(countries, HttpStatus.OK);
	}

	@GetMapping("/country/{countryId}/states")
	public ResponseEntity<Map<Integer, String>> getStates(@PathVariable Integer countryId) {
		Map<Integer, String> states = userMgmtService.getStatesForCountry(countryId);
		return new ResponseEntity<>(states, HttpStatus.OK);
	}

	@GetMapping("/state/{stateId}/cities")
	public ResponseEntity<Map<Integer, String>> getCities(@PathVariable Integer stateId) {
		Map<Integer, String> cities = userMgmtService.getCitiesForState(stateId);
		return new ResponseEntity<>(cities, HttpStatus.OK);
	}

}
