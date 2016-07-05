package de.hohenheim.sopranos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class QuizDuel extends DateClass {

	@Id
    @GeneratedValue
    private Integer quizId;
	
	@ManyToOne
	private SopraUser challenger;
	@ManyToOne
	private SopraUser challenged;
	@ManyToOne
	private Quiz challengerQuiz;
	@ManyToOne
	private Quiz challengedQuiz;
	@ManyToOne
	private LearningGroup learningGroup;
	
	
	public Integer getQuizId() {
		return quizId;
	}
	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}
	public SopraUser getChallenger() {
		return challenger;
	}
	public void setChallenger(SopraUser challenger) {
		this.challenger = challenger;
	}
	public SopraUser getChallenged() {
		return challenged;
	}
	public void setChallenged(SopraUser challenged) {
		this.challenged = challenged;
	}
	public Quiz getChallengerQuiz() {
		return challengerQuiz;
	}
	public void setChallengerQuiz(Quiz challengerQuiz) {
		this.challengerQuiz = challengerQuiz;
	}
	public Quiz getChallengedQuiz() {
		return challengedQuiz;
	}
	public void setChallengedQuiz(Quiz challengedQuiz) {
		this.challengedQuiz = challengedQuiz;
	}
	public LearningGroup getLearningGroup() {
		return learningGroup;
	}
	public void setLearningGroup(LearningGroup learningGroup) {
		this.learningGroup = learningGroup;
	}
	public boolean IsChallenger(SopraUser user){
		return user == challenger;
	}
	public boolean isDone(){
		return challengedQuiz.isDone() && challengerQuiz.isDone();
	}
	
}
