package de.hohenheim.sopranos.model;

import javax.persistence.*;

/**
 * Created by Burakhan on 07.06.2016.
 */

@Entity
public class Post {

    @Id
    @GeneratedValue
    Integer id;

    @Column(length=10000)
    String text;
 
    String heading;
    
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

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}
}