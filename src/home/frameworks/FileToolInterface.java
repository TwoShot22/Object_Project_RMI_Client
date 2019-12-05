package home.frameworks;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface FileToolInterface extends Remote {
	public void writeOnTxtFile(Vector<String> selectedItem, String fileName) throws RemoteException;
}
