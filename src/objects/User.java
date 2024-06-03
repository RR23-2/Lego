package objects;

import java.time.LocalDate;

public class User {
	protected String userID, username, userEmail, userPassword;
	protected LocalDate userDateOfBirth;
	public User(String userID, String username, String userEmail, String userPassword, LocalDate userDateOfBirth) {
		super();
		this.userID = userID;
		this.username = username;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userDateOfBirth = userDateOfBirth;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public LocalDate getUserDateOfBirth() {
		return userDateOfBirth;
	}
	public void setUserDateOfBirth(LocalDate userDateOfBirth) {
		this.userDateOfBirth = userDateOfBirth;
	}
}
