package de.hohenheim.sopranos.model;

import javax.persistence.Entity;

/**
 * Created by HP Brk on 22.06.2016.
 */
@Entity
public class McQuestion extends Question {

	private String[] answers;
	private boolean[] solutions;
	
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	public boolean[] getSolutions() {
		return solutions;
	}
	public void setSolutions(boolean[] solutions) {
		this.solutions = solutions;
	}
	
	
	
}
