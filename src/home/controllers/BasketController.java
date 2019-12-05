package home.controllers;

import java.io.FileNotFoundException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

import home.constant;
import home.frameworks.BasketInterface;
import home.frameworks.CheckDuplicationInterface;
import home.model.LectureModel;
import home.model.UserModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BasketController implements Initializable{
	private MainController controller;
	
	// Current User Data
	private String userID;
	private String userName;
	private String userCollege;
	private String userDepartment;
	private String userNumber;
	
	// Basket
	
	@FXML TableView<LectureController> basketTable;
	
	@FXML TableColumn<LectureController, Integer> basketNumberColumn;
	@FXML TableColumn<LectureController, String> basketNameColumn;
	@FXML TableColumn<LectureController, String> basketProfessorColumn;
	@FXML TableColumn<LectureController, Integer> basketCreditColumn;
	@FXML TableColumn<LectureController, String> basketTimeColumn;
	
	@FXML Button basketToRegister;
	@FXML Button basketDelete;
	@FXML Button basketRefresh;
	
	// Register
	
	@FXML TableView<LectureController> registerTable;
	
	@FXML TableColumn<LectureController, Integer> registerNumberColumn;
	@FXML TableColumn<LectureController, String> registerNameColumn;
	@FXML TableColumn<LectureController, String> registerProfessorColumn;
	@FXML TableColumn<LectureController, Integer> registerCreditColumn;
	@FXML TableColumn<LectureController, String> registerTimeColumn;
	
	@FXML Button registerToBasket;
	@FXML Button registerDelete;
	@FXML Button registerRefresh;
	
	// Table Datas
	private Vector<LectureController> basketControllers;
	ObservableList<LectureController> basketList = FXCollections.observableArrayList();
	private Object basketOldValue;
	
	private Vector<LectureController> registerControllers;
	ObservableList<LectureController> registerList = FXCollections.observableArrayList();
	private Object registerOldValue;
	
	// Left Control Bar
	@FXML Button lectureMove;
	@FXML Button userMove;
	@FXML Button loginMove;
	
	@FXML Label userNotification;
	
	// frameworks
	BasketInterface basketControl = null;
	CheckDuplicationInterface checkDuplication = null;
	
	
	public BasketController() throws RemoteException {
		this.controller = new MainController();
		try {
			checkDuplication = (CheckDuplicationInterface) constant.registry.lookup("checkduplication");
		} catch (NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {
			basketControl = (BasketInterface) constant.registry.lookup("basket");
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.checkCurrentUser();
	}
	
	// Check Login User
	public void checkCurrentUser() throws RemoteException {
		UserModel user = basketControl.checkCurrentUser();

		userID = user.getUserID();
		userName = user.getUserName();
		userCollege = user.getUserCollege();
		userDepartment = user.getUserDepartment();
		userNumber = user.getUserNumber();
	}
	
	public void initialize(URL location, ResourceBundle resources) {		
		this.userNotification.setText(this.userName+"님, 반갑습니다:)");
		
		// Basket
		basketTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		basketNumberColumn.setCellValueFactory(cellData->cellData.getValue().numberProperty().asObject());
		basketNameColumn.setCellValueFactory(cellData->cellData.getValue().nameProperty());
		basketProfessorColumn.setCellValueFactory(cellData->cellData.getValue().professorProperty());
		basketCreditColumn.setCellValueFactory(cellData->cellData.getValue().creditProperty().asObject());
		basketTimeColumn.setCellValueFactory(cellData->cellData.getValue().timeProperty());
		
		try {
			this.getBasketList(this.userID+"_Basket");
		} catch (FileNotFoundException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		basketTable.setOnMouseClicked(event->{
			if(basketTable.getSelectionModel().getSelectedItem()!=null) {
				if(event.getPickResult().getIntersectedNode().equals(basketOldValue)) {
					basketTable.getSelectionModel().clearSelection();
					basketOldValue = null;
				} else {
					basketOldValue = event.getPickResult().getIntersectedNode();
				}
			}
		});
		
		// Basket -> Register 강좌 이동 메소드
		basketToRegister.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Vector<String> selectedLecture = new Vector<>();
				Vector<String> selectedLectures = new Vector<>();
				ObservableList<LectureController> selectedItem = basketTable.getSelectionModel().getSelectedItems();
				
				for(int i=0;i<selectedItem.size();i++) {
					selectedLecture.add(String.valueOf(selectedItem.get(i).getNumber()));
					selectedLecture.add(selectedItem.get(i).getName());
					selectedLecture.add(selectedItem.get(i).getProfessor());
					selectedLecture.add(String.valueOf(selectedItem.get(i).getCredit()));
					selectedLecture.add(selectedItem.get(i).getTime());
				}
				
				// Make Sentence to Compare
				for(int i=0;i<selectedLecture.size();i+=5) {
					selectedLectures.add(selectedLecture.get(i)+" "+selectedLecture.get(i+1)+" "+selectedLecture.get(i+2)+" "+selectedLecture.get(i+3)+" "+selectedLecture.get(i+4));
				}
				
				String lectureMessage = "";
				
				// Alert Method
				if(selectedLectures.size()==0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Occured");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nNo Allowed");
					alert.setContentText("Please Select Lecture you want to move to Register!");
					alert.show();
				} else {
					// selectedItem != null
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm Sending Lecture to Register");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nAre you sure you want to put Lectures in Register?");
					
					for(int i=0;i<selectedLectures.size();i++) {
						lectureMessage+=(selectedLectures.get(i)+"\n");
					}
					alert.setContentText(lectureMessage);
					
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get()==ButtonType.OK) {
						for(int i=0; i<selectedLectures.size();i++) {
							try {
								checkDuplication.manageLectureFile(selectedLectures.get(i),"data/user/"+userID+"_Register","AddLecture");
								checkDuplication.manageLectureFile(selectedLectures.get(i),"data/user/"+userID+"_Basket","DeleteLecture");			
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						}
						
						try {
							getBasketList(userID+"_Basket");
							getRegisterList(userID+"_Register");
							basketTable.refresh();
							registerTable.refresh();
							basketTable.getSelectionModel().clearSelection();
							registerTable.getSelectionModel().clearSelection();
						} catch (FileNotFoundException | RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Alert success = new Alert(AlertType.INFORMATION);
						success.setTitle("Result Notification");
						success.setHeaderText("Success to Send Lectures in Register");
						success.setContentText("성공");
						success.show();
					} else {
						Alert cancel = new Alert(AlertType.ERROR);
						cancel.setTitle("Result Notification");
						cancel.setHeaderText("Fail to Send Lectures in Register");
						cancel.setContentText("실패");
						cancel.show();
					}
				}
			}
		});
		
		basketDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Vector<String> selectedLecture = new Vector<>();
				Vector<String> selectedLectures = new Vector<>();
				ObservableList<LectureController> selectedItem = basketTable.getSelectionModel().getSelectedItems();
				
				for(int i=0;i<selectedItem.size();i++) {
					selectedLecture.add(String.valueOf(selectedItem.get(i).getNumber()));
					selectedLecture.add(selectedItem.get(i).getName());
					selectedLecture.add(selectedItem.get(i).getProfessor());
					selectedLecture.add(String.valueOf(selectedItem.get(i).getCredit()));
					selectedLecture.add(selectedItem.get(i).getTime());
				}
				
				for(int i=0;i<selectedLecture.size();i+=5) {
					selectedLectures.add(selectedLecture.get(i)+" "+selectedLecture.get(i+1)+" "+selectedLecture.get(i+2)+" "+selectedLecture.get(i+3)+" "+selectedLecture.get(i+4));
				}
				
				String lectureMessage = "";
				
				if(selectedLectures.size()==0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Occured");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nNo Allowed");
					alert.setContentText("Please Select Lecture you want to Delete!");
					alert.show();
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm Delete Lecture");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nAre you sure you want to [Delete] Lectures in Basket?");
					for(int i=0;i<selectedLectures.size();i++) {
						lectureMessage+=(selectedLectures.get(i)+"\n");
					}
					alert.setContentText(lectureMessage);
					
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get()==ButtonType.OK) {
						for(int i=0; i<selectedLectures.size();i++) {
							try {
								checkDuplication.manageLectureFile(selectedLectures.get(i),"data/user/"+userID+"_Basket","DeleteLecture");
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}					
						}
						
						try {
							getBasketList(userID+"_Basket");
							basketTable.refresh();
							basketTable.getSelectionModel().clearSelection();
						} catch (FileNotFoundException | RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Alert success = new Alert(AlertType.INFORMATION);
						success.setTitle("Result Notification");
						success.setHeaderText("Success to Delete Lectures in Basket");
						success.setContentText("성공");
						success.show();
					} else {
						Alert cancel = new Alert(AlertType.ERROR);
						cancel.setTitle("Result Notification");
						cancel.setHeaderText("Fail to Delete Lectures in Basket");
						cancel.setContentText("실패");
						cancel.show();
					}
				}
			}
		});
		
		basketRefresh.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				basketTable.getSelectionModel().clearSelection();
			}
		});
		
		// Register
		registerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		registerNumberColumn.setCellValueFactory(cellData->cellData.getValue().numberProperty().asObject());
		registerNameColumn.setCellValueFactory(cellData->cellData.getValue().nameProperty());
		registerProfessorColumn.setCellValueFactory(cellData->cellData.getValue().professorProperty());
		registerCreditColumn.setCellValueFactory(cellData->cellData.getValue().creditProperty().asObject());
		registerTimeColumn.setCellValueFactory(cellData->cellData.getValue().timeProperty());
		
		try {
			this.getRegisterList(this.userID+"_Register");
		} catch (FileNotFoundException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registerTable.setOnMouseClicked(event->{
			if(registerTable.getSelectionModel().getSelectedItem()!=null) {
				if(event.getPickResult().getIntersectedNode().equals(registerOldValue)) {
					registerTable.getSelectionModel().clearSelection();
					registerOldValue = null;
				} else {
					registerOldValue = event.getPickResult().getIntersectedNode();
				}
			}
		});
		
		registerToBasket.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Vector<String> selectedLecture = new Vector<>();
				Vector<String> selectedLectures = new Vector<>();
				ObservableList<LectureController> selectedItem = registerTable.getSelectionModel().getSelectedItems();
				
				for(int i=0;i<selectedItem.size();i++) {
					selectedLecture.add(String.valueOf(selectedItem.get(i).getNumber()));
					selectedLecture.add(selectedItem.get(i).getName());
					selectedLecture.add(selectedItem.get(i).getProfessor());
					selectedLecture.add(String.valueOf(selectedItem.get(i).getCredit()));
					selectedLecture.add(selectedItem.get(i).getTime());
				}
				
				for(int i=0;i<selectedLecture.size();i+=5) {
					selectedLectures.add(selectedLecture.get(i)+" "+selectedLecture.get(i+1)+" "+selectedLecture.get(i+2)+" "+selectedLecture.get(i+3)+" "+selectedLecture.get(i+4));
				}
				
				String lectureMessage = "";
				
				if(selectedLectures.size()==0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Occured");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nNo Allowed");
					alert.setContentText("Please Select Lecture you want to Throw Away to Basket!");
					alert.show();
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm Sending Lecture");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nAre you sure you want to throw away Lectures in Basket?");
					for(int i=0;i<selectedLectures.size();i++) {
						lectureMessage+=(selectedLectures.get(i)+"\n");
					}
					alert.setContentText(lectureMessage);
					
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get()==ButtonType.OK) {
						for(int i=0; i<selectedLectures.size();i++) {
							try {
								checkDuplication.manageLectureFile(selectedLectures.get(i),"data/user/"+userID+"_Basket","AddLecture");
								checkDuplication.manageLectureFile(selectedLectures.get(i),"data/user/"+userID+"_Register","DeleteLecture");					
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}					
						}
						
						try {
							getBasketList(userID+"_Basket");
							getRegisterList(userID+"_Register");
							basketTable.refresh();
							registerTable.refresh();
							basketTable.getSelectionModel().clearSelection();
							registerTable.getSelectionModel().clearSelection();
						} catch (FileNotFoundException | RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Alert success = new Alert(AlertType.INFORMATION);
						success.setTitle("Result Notification");
						success.setHeaderText("Success to Throw Away Lectures in Basket");
						success.setContentText("성공");
						success.show();
					} else {
						Alert cancel = new Alert(AlertType.ERROR);
						cancel.setTitle("Result Notification");
						cancel.setHeaderText("Fail to Throw Away Lectures in Basket");
						cancel.setContentText("실패");
						cancel.show();
					}
				}
				
			}
		});
		
		registerDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Vector<String> selectedLecture = new Vector<>();
				Vector<String> selectedLectures = new Vector<>();
				ObservableList<LectureController> selectedItem = registerTable.getSelectionModel().getSelectedItems();
				
				for(int i=0;i<selectedItem.size();i++) {
					selectedLecture.add(String.valueOf(selectedItem.get(i).getNumber()));
					selectedLecture.add(selectedItem.get(i).getName());
					selectedLecture.add(selectedItem.get(i).getProfessor());
					selectedLecture.add(String.valueOf(selectedItem.get(i).getCredit()));
					selectedLecture.add(selectedItem.get(i).getTime());
				}
				
				for(int i=0;i<selectedLecture.size();i+=5) {
					selectedLectures.add(selectedLecture.get(i)+" "+selectedLecture.get(i+1)+" "+selectedLecture.get(i+2)+" "+selectedLecture.get(i+3)+" "+selectedLecture.get(i+4));
				}
				
				String lectureMessage = "";
				
				if(selectedLectures.size()==0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Occured");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nNo Allowed");
					alert.setContentText("Please Select Lecture you want to Delete!");
					alert.show();
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm Delete Lecture");
					alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nAre you sure you want to [Delete] Lectures in Register?");
					for(int i=0;i<selectedLectures.size();i++) {
						lectureMessage+=(selectedLectures.get(i)+"\n");
					}
					alert.setContentText(lectureMessage);
					
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get()==ButtonType.OK) {
						for(int i=0; i<selectedLectures.size();i++) {
							try {
								checkDuplication.manageLectureFile(selectedLectures.get(i),"data/user/"+userID+"_Register","DeleteLecture");
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}					
						}
						
						try {
							getRegisterList(userID+"_Register");
							registerTable.refresh();
							basketTable.getSelectionModel().clearSelection();
							registerTable.getSelectionModel().clearSelection();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Alert success = new Alert(AlertType.INFORMATION);
						success.setTitle("Result Notification");
						success.setHeaderText("Success to Delete Lectures in Register");
						success.setContentText("성공");
						success.show();
					} else {
						Alert cancel = new Alert(AlertType.ERROR);
						cancel.setTitle("Result Notification");
						cancel.setHeaderText("Fail to Delete Lectures in Register");
						cancel.setContentText("실패");
						cancel.show();
					}
				}
			}
		});
		
		registerRefresh.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				registerTable.getSelectionModel().clearSelection();
			}
		});
		
		// Control Bar
		lectureMove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				handleLectureMoveAction(event);
			}
		});
		
		userMove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleUserMoveAction(event);
			}
		});
		
		loginMove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				handleLoginMoveAction(event);
			}
		});
	}
	
	// Load Data Method
	private Vector<LectureController> getBasketData(String fileName) throws FileNotFoundException, RemoteException{
		Vector<LectureModel> lectureModels = basketControl.getBasketData(fileName); // 파일에서 과목 정보 가져오기
		Vector<LectureController> lectureControllers = new Vector<LectureController>(); // 실제 표현데이터 형식
		LectureController lectureController;
		
		for(LectureModel lectureModel: lectureModels) {
			lectureController = new LectureController();
			
			lectureController.setNumber(lectureModel.getNumber());
			lectureController.setName(lectureModel.getName());
			lectureController.setProfessor(lectureModel.getProfessor());
			lectureController.setCredit(lectureModel.getCredit());
			lectureController.setTime(lectureModel.getTime());
			
			lectureControllers.add(lectureController);
		}
		return lectureControllers;
	} 
	
	public void getBasketList(String fileName) throws FileNotFoundException, RemoteException {		
		for(int i=0;i<basketTable.getItems().size();i++) {
			basketTable.getItems().clear();
		}
		
		basketControllers = getBasketData("data/user/"+fileName);
		
		basketList.clear();
		
		for(LectureController basketController: basketControllers) {
			basketTable.getItems().add(new LectureController(new SimpleIntegerProperty(basketController.getNumber()), new SimpleStringProperty(basketController.getName()), 
					new SimpleStringProperty(basketController.getProfessor()), new SimpleIntegerProperty(basketController.getCredit()), new SimpleStringProperty(basketController.getTime())));
		}
	}
	
	private Vector<LectureController> getRegisterData(String fileName) throws FileNotFoundException, RemoteException{
		Vector<LectureModel> lectureModels = basketControl.getRegisterData(fileName); // 파일에서 과목 정보 가져오기
		Vector<LectureController> lectureControllers = new Vector<LectureController>(); // 실제 표현데이터 형식
		LectureController lectureController;
		
		for(LectureModel lectureModel: lectureModels) {
			lectureController = new LectureController();
			
			lectureController.setNumber(lectureModel.getNumber());
			lectureController.setName(lectureModel.getName());
			lectureController.setProfessor(lectureModel.getProfessor());
			lectureController.setCredit(lectureModel.getCredit());
			lectureController.setTime(lectureModel.getTime());
			
			lectureControllers.add(lectureController);
		}
		return lectureControllers;
	} 
	
	private void getRegisterList(String fileName) throws FileNotFoundException, RemoteException {		
		for(int i=0;i<registerTable.getItems().size();i++) {
			registerTable.getItems().clear();
		}
		
		registerControllers = getRegisterData("data/user/"+fileName);
		
		registerList.clear();
		
		for(LectureController registerController: registerControllers) {
			registerTable.getItems().add(new LectureController(new SimpleIntegerProperty(registerController.getNumber()), new SimpleStringProperty(registerController.getName()), 
					new SimpleStringProperty(registerController.getProfessor()), new SimpleIntegerProperty(registerController.getCredit()), new SimpleStringProperty(registerController.getTime())));
		}
	}
	
	// Control Bar Method
		public void handleLectureMoveAction(ActionEvent event) {
			this.controller.loadStage("src/home/fxml/Table.fxml","명지대학교 수강신청 시스템");
			Stage basket = (Stage)lectureMove.getScene().getWindow();
			basket.close();
		}
		
		public void handleUserMoveAction(ActionEvent event) {
			this.controller.loadStage("src/home/fxml/UserInfo.fxml", "명지대학교 수강신청 시스템");
		}
		
		public void handleLoginMoveAction(ActionEvent event) {
			this.controller.loadStage("src/home/fxml/Login.fxml", "명지대학교 수강신청 시스템");
			Stage basket = (Stage)loginMove.getScene().getWindow();
			basket.close();
		}
}