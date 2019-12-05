package home.model;

import java.io.Serializable;
import java.util.Scanner;

public class LectureModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;
	private String name;
	private String professor;
	private int credit;
	private String time;
	
	public void read(Scanner scanner) {
		this.number = scanner.nextInt();
		this.name = scanner.next();
		this.professor = scanner.next();
		this.credit = scanner.nextInt();
		this.time = scanner.next();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}