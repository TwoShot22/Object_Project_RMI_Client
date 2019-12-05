package home.frameworks;

import java.rmi.Remote;
import java.rmi.RemoteException;

import home.model.UserModel;

public interface UserInfoInterface extends Remote {
	
	public UserModel checkCurrentUser() throws RemoteException;
}
