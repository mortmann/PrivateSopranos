package de.hohenheim.sopranos.model;

import javax.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP Brk on 22.06.2016.
 */
@Entity
public class Quiz extends DateClass {

    @Id
    @GeneratedValue
    private Integer quizId;
    @ManyToOne
    private SopraUser generated;
    private int points=0;
    private boolean[][] answers;
    private String[] answertext; 
    private boolean[] answercorrected; 
    
    private boolean isPartOfDuel = false;

    private boolean done = false;
	@ManyToMany
    @JoinTable(
            name = "QUIZQUESTIONS",
            joinColumns = @JoinColumn(name = "QUIZ_ID"),
            inverseJoinColumns = @JoinColumn(name = "QUEST_ID"))
    private List<Question> questList = new ArrayList<>();
    
	public Quiz(){
		    	setCreateDate();
	}
	
    public List<Question> getQuestList() { 
		return questList;
	}

	public void setQuestList(List<Question> questList){
    	this.questList=questList;
    }

	public Integer getQuizId() {
		return quizId;
	}

	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}

	public SopraUser getGenerated() {
		return generated;
	}

	public void setGenerated(SopraUser generated) {
		this.generated = generated;
	}

	public String[][] getAnswers() {
		String[][] s = new String[answers.length][];
		for (int i = 0; i<answers.length;i++) {
			for (int x = 0; x<answers[i].length;x++) {
				if(s[i]==null){
					s[i] = new String[answers[i].length];
				}
				s[i][x] = Boolean.toString(answers[i][x]);
			}
		}
		return s;
	}
	public boolean[][] getAnswersBoolean() {
		return answers;
	}
	public void setAnswers(boolean[][] answers) {
		this.answers = answers;
	}

	public void addAnswer(int number,Answer answer){
		if(answers==null){
			answers=new boolean[questList.size()][];
		}
		if(questList.get(number-1).isOpenQuestion()==false){
			answers[number-1]=answer.getBooleans();
		} else {
			answers=new boolean[1][1];

			System.out.println("add answertext " + answer.getAnswertext0());
			setAnswertext(number-1,answer.getAnswertext0());
		}
	}

	public String[] getAnswertext() {
		return answertext;
	}

	public void setAnswertext(int number,String answer) {
		if(answercorrected==null){
			answercorrected=new boolean[questList.size()];
		}
		if(answertext==null){
			this.answertext = new String[questList.size()];
			for (int i = 0; i < answers.length; i++) {
				answertext[i] = new String();
			}
		}
		this.answertext[number] = answer;
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isPartOfDuel() {
		return isPartOfDuel;
	}

	public void setPartOfDuel(boolean isPartOfDuel) {
		this.isPartOfDuel = isPartOfDuel;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public void setSolution(int number, boolean answer) {
		System.out.println(points + "p b" + number + " " + answer);
		if(answer==true){
			points++;
		} else {
			points--;
		}
		if(answercorrected==null){
			answercorrected = new boolean[questList.size()];
			answercorrected[number] = true;
		}
		System.out.println(points + "p");
		answers[number][0] = answer;
	}

	public boolean[] getAnswercorrected() {
		if(answercorrected==null){
			answercorrected=new boolean[questList.size()];
		}
		return answercorrected;
	}

	public void setAnswercorrected(boolean[] answercorrected) {
		
		this.answercorrected = answercorrected;
	}
	
	
}
