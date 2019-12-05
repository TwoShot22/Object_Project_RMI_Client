package home.frameworks;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import home.model.DirectoryModel;
import home.model.LectureModel;
import home.model.UserModel;

public interface TableInterface extends Remote {
	
    public UserModel checkCurrentUser() throws RemoteException;
    // Directory Methods

    // [Campus] File Read and Add Items
    public Vector<DirectoryModel> getCampusData(String fileName) throws FileNotFoundException, RemoteException;

    // [College] File Read and Add Items
    public Vector<DirectoryModel> getCollegeData(String fileName) throws FileNotFoundException, RemoteException;
    
    // [Department] File Read and Add Items
    public Vector<DirectoryModel> getDepartmentData(String fileName) throws FileNotFoundException, RemoteException;

    // Lecture Methods
    public Vector<LectureModel> getLectureData(String fileName) throws FileNotFoundException, RemoteException;
}