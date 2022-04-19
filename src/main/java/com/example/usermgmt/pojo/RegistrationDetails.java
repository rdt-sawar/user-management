package com.example.usermgmt.pojo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Data;

@Data
public class RegistrationDetails {
	
	private String fname ;
	private String lname;
	private String email;
	
	private String password;
	private String phone;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dob;
	
	private char gender;
	private Integer cityId;
	private Integer stateId;
	private Integer countryId;
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getStateId() {
		return stateId;
	}
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	@Override
	public String toString() {
		return "RegistrationDetails [fname=" + fname + ", lname=" + lname + ", email=" + email + ", password="
				+ password + ", phone=" + phone + ", dob=" + dob + ", gender=" + gender + ", cityId=" + cityId
				+ ", stateId=" + stateId + ", countryId=" + countryId + "]";
	}
	
	
	

}
