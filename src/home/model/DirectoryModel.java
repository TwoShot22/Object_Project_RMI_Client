package home.model;

import java.io.Serializable;
import java.util.Scanner;

public class DirectoryModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;
	private String name;
	private String hyperLink;
	
	public void read(Scanner scanner) {
		this.number = scanner.nextInt();
		this.name = scanner.next();
		this.hyperLink = scanner.next();
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getHyperLink() {
		return this.hyperLink;
	}
}
