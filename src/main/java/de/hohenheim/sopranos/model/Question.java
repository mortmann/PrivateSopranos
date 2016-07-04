package de.hohenheim.sopranos.model;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP Brk on 22.06.2016.
 */
@Transactional
@Entity
public class Question extends DateClass {

    @Id
    @GeneratedValue
    private Integer questId;

    @Column(length = 10000)
    private String questText;

    private Boolean quizQuest = false;

    private float rating = 0;
    private int ratingCount = 0;


	private String[] answers;
	private boolean[] solutions;

    @OneToMany(mappedBy = "question")
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    private SopraUser sopraUser;

    @ManyToOne
    private LearningGroup learningGroup;

    @ManyToMany(mappedBy = "questList")
    private List<Quiz> quizList = new ArrayList<>();


    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public String getQuestText() {
        return questText;
    }

    public void setQuestText(String questText) {
        this.questText = questText;
    }

    public Boolean getQuizQuest() {
        return quizQuest;
    }

    public void setQuizQuest(Boolean quizQuest) {
        this.quizQuest = quizQuest;
    }

    public float getRating() {
    	if(rating==0){
    		return 0;
    	}
        return rating/ratingCount;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public void addRating(float rating) {
        this.rating += rating;
        ratingCount++;
    }

    public SopraUser getSopraUser() {
        return sopraUser;
    }

    public void setSopraUser(SopraUser sopraUser) {
        this.sopraUser = sopraUser;
    }

    public LearningGroup getLearningGroup() {
        return learningGroup;
    }

    public void setLearningGroup(LearningGroup learningGroup) {
        this.learningGroup = learningGroup;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

	
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
    public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}

	public List<Comment> getCommentList() {return commentList;}

    public void setCommentList(List<Comment> commentList) {this.commentList = commentList;}
    
    public boolean questioncorrected(boolean[] b){
    	for (int s = 0; s < getSolutions().length; s++) {
			if(b[s]!=getSolutions()[s]){
				return false;
			}
		}
    	return true;
    }
    
}
