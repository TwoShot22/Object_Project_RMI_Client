package home.frameworks;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CheckDuplicationInterface extends Remote {

	public boolean checkOverLapData(String str1, String str2) throws RemoteException;
	public void manageLectureFile(String selectedLectures, String targetFileName, String mode) throws RemoteException;
	public void manageUserFile(String userInfo, String TargetFileName) throws RemoteException;
	public void manageCurrentUser(String userInfo, String TargetFileName) throws RemoteException;
}
