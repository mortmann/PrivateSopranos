package de.hohenheim.sopranos.model;

import javax.persistence.*;

@Entity 
public class UserEvent extends DateClass{
	
	@Id
	@GeneratedValue
	private int id;
	
	private String text;
	
	@ManyToOne
	private LearningGroup lg;
	
	@ManyToOne
	private SopraUser sopraUser;
	public UserEvent(){
    	setCreateDate();
    }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public SopraUser getSopraUser() {
		return sopraUser;
	}

	public void setSopraUser(SopraUser sopraUser) {
		this.sopraUser = sopraUser;
	}

	public LearningGroup getLg() {
		return lg;
	}

	public void setLg(LearningGroup lg) {
		this.lg = lg;
	}
	
}
