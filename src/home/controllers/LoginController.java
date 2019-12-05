package home.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import home.constant;
import home.frameworks.CheckDuplicationInterface;
import home.frameworks.LoginInterface;
import home.model.UserModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	@FXML private Button loginButton;
	@FXML private Button signUpButton;
	@FXML private CheckBox saveUserName;
	
	@FXML private TextField loginTextField;
	@FXML private PasswordField loginPasswordField;
	
	@FXML private Label loginErrorMessage;
	
	private MainController controller;
	
	// ����� �Է� ID, Password
	private String inputID;
	private String inputPassword;
	
	// DB�� �ִ� ID, Password
	private String dataID;
	private String dataPassword;
	private String dataName;
	private String dataCollege;
	private String dataDepartment;
	private String dataNumber;
	
	// ID ����
	private String tempID;
	private boolean saveMode = false;
	
	public LoginController() {
		this.controller = new MainController();
	}
	
	// frameworks
	LoginInterface loginControl = null;
	CheckDuplicationInterface checkDuplication = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			loginControl = (LoginInterface) constant.registry.lookup("login");
			checkDuplication = (CheckDuplicationInterface) constant.registry.lookup("checkduplication");
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Action Handler ���
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleLoginButtonAction(event);
			}
		});
		
		signUpButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleSignUpButtonAction(event);
			}
		});
		
		loginTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode().equals(KeyCode.ENTER)) {
					handleLoginButtonAction(event);
				}
			}
		});
		
		loginPasswordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode().equals(KeyCode.ENTER)) {
					handleLoginButtonAction(event);
				}
			}
		});
				
		saveUserName.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
				// TODO Auto-generated method stub
				saveUserNameAction(new_val);
			}
		});
	}
	
	// Login Button Ŭ�� �� Action ����
	public void handleLoginButtonAction(ActionEvent event) {
		this.loginProcess();
	}
	
	public void handleLoginButtonAction(KeyEvent event) {
		this.loginProcess();
	}
	
	// Sign Up Button Ŭ�� �� Action ����
	public void handleSignUpButtonAction(ActionEvent event) {
		this.controller.loadStage("src/home/fxml/SignUp.fxml","�������б� ������û �ý���");
		signUpButton.getScene().getWindow();
	}
	
	private void loginProcess() {
		// Login ���� Check
		boolean loginCheck;
				
		// ID, Password Field �� ��������
		this.inputID = loginTextField.getText();
		this.inputPassword = loginPasswordField.getText();
				
		// Login ���ο� ���� Action ����
		try {
			loginCheck = this.authenticate(inputID, inputPassword);
			
			UserModel user = loginControl.getUser();

			dataID = user.getUserID();
			dataPassword = user.getUserPassword();
			dataName = user.getUserName();
			dataCollege = user.getUserCollege();
			dataDepartment = user.getUserDepartment();
			dataNumber = user.getUserNumber();
					
			if(loginCheck) {
				String userInfo = this.dataID + " "+this.dataName+" "+this.dataCollege + " "+this.dataDepartment + " "+this.dataNumber;
				checkDuplication.manageCurrentUser(userInfo, "data/user/CurrentUser");
				
				this.controller.loadStage("src/home/fxml/Table.fxml","�������б� ������û �ý���");
				Stage login = (Stage)loginButton.getScene().getWindow();
				login.close();
			} else {
				loginErrorMessage.setText("Invalid UserName or Password");
				if(this.saveMode) {
					loginTextField.setText(tempID);
				} else {
					loginTextField.setText(null);					
				}
				
				loginPasswordField.setText(null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Login DB�� ����� �Է°� �� �޼ҵ�
	public boolean authenticate(String inputID, String inputPassword) throws FileNotFoundException, RemoteException{
		return loginControl.authenticate(inputID, inputPassword);
	}
	
	// ID ���� �޼ҵ�
	private void saveUserNameAction(Boolean val) {
		this.saveMode = val;
		
		if(this.saveMode) {
			this.tempID = loginTextField.getText();			
		} else {
			this.tempID = null;
			loginTextField.setText(null);
		}
	}
}