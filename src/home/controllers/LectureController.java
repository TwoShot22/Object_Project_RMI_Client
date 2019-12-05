package home.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LectureController {
	private final SimpleIntegerProperty number;
	private final SimpleStringProperty name;
	private final SimpleStringProperty professor;
	private final SimpleIntegerProperty credit;
	private final SimpleStringProperty time;

	public LectureController() {
		this.number = new SimpleIntegerProperty();
		this.name = new SimpleStringProperty();
		this.professor = new SimpleStringProperty();
		this.credit = new SimpleIntegerProperty();
		this.time = new SimpleStringProperty();
	}

	public LectureController(SimpleIntegerProperty number, SimpleStringProperty name, SimpleStringProperty professor, SimpleIntegerProperty credit, SimpleStringProperty time) {
		this.number = number;
		this.name = name;
		this.professor = professor;
		this.credit = credit;
		this.time = time;
	}
	
	public SimpleIntegerProperty numberProperty() {
		return number;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	
	public SimpleStringProperty professorProperty() {
		return professor;
	}
	
	public SimpleIntegerProperty creditProperty() {
		return credit;
	}
	
	public SimpleStringProperty timeProperty() {
		return time;
	}
	
	public int getNumber() {
		return number.get();
	}
	
	public void setNumber(int number) {
		this.number.set(number);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public String getProfessor() {
		return professor.get();
	}
	
	public void setProfessor(String professor) {
		this.professor.set(professor);
	}
	
	public int getCredit() {
		return credit.get();
	}
	
	public void setCredit(int credit) {
		this.credit.set(credit);
	}
	
	public String getTime() {
		return time.get();
	}
	
	public void setTime(String time) {
		this.time.set(time);
	}
}
