package de.hohenheim.sopranos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Burakhan on 07.06.2016.
 */

@Entity
public class Post {

    @Id
    @GeneratedValue
    Integer id;

    String text;
 
    public Post() {}

    public Post(String text) {
        this.text = text;
    } 

    @ManyToOne
    public LearningGroup learningGroup;

    @ManyToOne
    public SopraUser sopraUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LearningGroup getLearningGroup() {return learningGroup;}

    public void setLearningGroup(LearningGroup learningGroup) {this.learningGroup = learningGroup;}

    public SopraUser getSopraUser() {return sopraUser;}

    public void setSopraUser(SopraUser sopraUser) {this.sopraUser = sopraUser;}
}