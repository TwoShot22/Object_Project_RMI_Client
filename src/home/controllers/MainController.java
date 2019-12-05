package home.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainController implements Initializable {
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void loadStage(String fxml, String title) {
		try {
			URL fxmlPath = new File(fxml).toURL();
			
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(fxmlPath);
			
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(title);
	        stage.getIcons().add(new Image("images/Myongji_Logo.gif"));
			stage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}