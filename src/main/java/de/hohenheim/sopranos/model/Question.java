package de.hohenheim.sopranos.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
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

    private float rating = 0;
    private int ratingCount = 0;


    private String[] answers;
    private boolean[] solutions;

    @OneToMany(mappedBy = "question")
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    private SopraUser sopraUser;

    @ManyToOne
    private LearningGroup notReleased;

    @ManyToOne
    private LearningGroup released;

    @ManyToMany(mappedBy = "questList")
    private List<Quiz> quizList = new ArrayList<>();

    private boolean openQuestion;

    public Question() {
        setCreateDate();
    }

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

    public float getRating() {
        if (rating == 0) {
            return 0;
        }
        return rating / ratingCount;
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

    public LearningGroup getNotReleased() {
        return notReleased;
    }

    public void setNotReleased(LearningGroup notReleased) {
        this.notReleased = notReleased;
    }

    public LearningGroup getReleased() {
        return released;
    }

    public void setReleased(LearningGroup released) {
        this.released = released;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
        if (answers.length == 1) {
            openQuestion = true;
        }
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public boolean isOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(boolean openQuestion) {
        this.openQuestion = openQuestion;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public boolean questioncorrected(boolean[] b) {
        for (int s = 0; s < getSolutions().length; s++) {
            if (b[s] != getSolutions()[s]) {
                return false;
            }
        }
        return true;
    }

}
