package home.frameworks;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SignUpInterface extends Remote {
	
	public boolean idAuthenticate(String inputID) throws FileNotFoundException, RemoteException;
	
	public boolean personAuthenticate(String inputNumber) throws FileNotFoundException, RemoteException;
	
	public void createUserData(String id) throws RemoteException;
}