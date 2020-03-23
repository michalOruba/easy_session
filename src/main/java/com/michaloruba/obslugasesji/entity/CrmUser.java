package com.michaloruba.obslugasesji.entity;

import com.michaloruba.obslugasesji.validation.FieldMatch;
import com.michaloruba.obslugasesji.validation.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
})
public class CrmUser {

	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	private String userName;

	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	private String password;
	
	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	private String matchingPassword;

	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	private String firstName;

	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	private String lastName;

	@ValidEmail
	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	private String email;

	public CrmUser() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "CrmUser{" +
				"userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", matchingPassword='" + matchingPassword + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
