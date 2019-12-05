package home.model;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentsModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SimpleStringProperty userID;
	private SimpleStringProperty userPassword;
	private SimpleStringProperty userName;
	private SimpleStringProperty userCollege;
	private SimpleStringProperty userDepartment;
	private SimpleStringProperty userNumber;
	
	public StudentsModel(String userID, String userPassword) {
		this.userID = new SimpleStringProperty(userID);
		this.userPassword = new SimpleStringProperty(userPassword);
	}
	
	public StudentsModel(String userID, String userPassword, String userName, String userCollege, String userDepartment, String userNumber) {
		this.userID = new SimpleStringProperty(userID);
		this.userPassword = new SimpleStringProperty(userPassword);
		this.userName = new SimpleStringProperty(userName);
		this.userCollege = new SimpleStringProperty(userCollege);
		this.userDepartment = new SimpleStringProperty(userDepartment);
		this.userNumber = new SimpleStringProperty(userNumber);
	}
	
	public String getUserID() {
		return userID.get();
	}
	
	public void setUserID(String userID) {
		this.userID = new SimpleStringProperty(userID);
	}
	
	public StringProperty textProperty() {return userID;}
	
	public String getUserPassword() {
		return userPassword.get();
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = new SimpleStringProperty(userPassword);
	}
	
	public String getUserName() {
		return userName.get();
	}
	
	public void setUserName(String userName) {
		this.userName = new SimpleStringProperty(userName);
	}
	
	public String getUserCollege() {
		return userCollege.get();
	}
	
	public void setUserCollege(String userCollege) {
		this.userCollege = new SimpleStringProperty(userCollege);
	}
	
	public String getUserDepartment() {
		return userDepartment.get();
	}
	
	public void setUserDepartment(String userDepartment) {
		this.userDepartment = new SimpleStringProperty(userDepartment);
	}
	
	public String getUserNumber() {
		return userNumber.get();
	}
	
	public void setUserNumber(String userNumber) {
		this.userNumber = new SimpleStringProperty(userNumber);
	}
}
