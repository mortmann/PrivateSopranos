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
public abstract class Question {

    @Id
    @GeneratedValue
    private Integer questId;

    @Column(length = 10000)
    private String questText;

    private Boolean quizQuest = false;

    private int rating;



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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public List<Comment> getCommentList() {return commentList;}

    public void setCommentList(List<Comment> commentList) {this.commentList = commentList;}
}
