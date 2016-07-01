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
    
    private Answer[] answers;
    

	@ManyToMany
    @JoinTable(
            name = "QUIZQUESTIONS",
            joinColumns = @JoinColumn(name = "QUIZ_ID"),
            inverseJoinColumns = @JoinColumn(name = "QUEST_ID"))
    private List<Question> questList = new ArrayList<>();
    
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

	public Answer[] getAnswers() {
		return answers;
	}

	public void setAnswers(Answer[] answers) {
		this.answers = answers;
	}

	public void addAnswer(int number,Answer answer){
		answers[number]=answer;
	}
	
	
}
