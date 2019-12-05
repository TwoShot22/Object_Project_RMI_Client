package home;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent login = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        primaryStage.setTitle("명지대학교 수강신청 시스템");
        primaryStage.getIcons().add(new Image("images/Myongji_Logo.gif"));
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
	}

	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		try {
			constant.registry = LocateRegistry.getRegistry(host);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}
}
