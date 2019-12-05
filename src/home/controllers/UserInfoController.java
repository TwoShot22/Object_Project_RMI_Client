package home.controllers;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import home.constant;
import home.frameworks.UserInfoInterface;
import home.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserInfoController implements Initializable{
	private MainController controller;
	
	@FXML Button closeWindowButton;
	
	@FXML Label nameLabel;
	@FXML Label numberLabel;
	@FXML Label collegeLabel;
	@FXML Label departmentLabel;	
	
	@FXML Label maxCreditLabel;
	@FXML Label currentCreditLabel;
	@FXML Label currentLectureLabel;
	
	// Current User Data
	private String userID;
	private String userName;
	private String userCollege;
	private String userDepartment;
	private String userNumber;
	
	// UserInfoControl
	UserInfoInterface userInfoControl = null;
		
	public UserInfoController() {
		
		try {
			userInfoControl = (UserInfoInterface) constant.registry.lookup("userinfo");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.controller = new MainController();
		
		try {
			this.checkCurrentUser();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkCurrentUser() throws RemoteException {
		UserModel user = userInfoControl.checkCurrentUser();

		userID = user.getUserID();
		userName = user.getUserName();
		userCollege = user.getUserCollege();
		userDepartment = user.getUserDepartment();
		userNumber = user.getUserNumber();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleCloseWindowButton(event);
			}
		});
		
		nameLabel.setText("Name : "+this.userName);
		numberLabel.setText("Number : "+this.userNumber);
		collegeLabel.setText("College : "+this.userCollege);
		departmentLabel.setText("Dept. : "+this.userDepartment);
	}
	
	private void handleCloseWindowButton(ActionEvent event) {
		Stage userInfo = (Stage)closeWindowButton.getScene().getWindow();
		userInfo.close();
	}
}
