package de.hohenheim.sopranos.model;

import javax.persistence.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burakhan on 07.06.2016.
 */
@Entity
public class Post extends DateClass{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 10000)
    private String text;

    private String heading;

    private float rating;
    private int ratingCount;
    @Transient
    private MultipartFile file;
    @ManyToOne
    private LearningGroup learningGroup;

    @ManyToOne
    private SopraUser sopraUser;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();



    public Post() {}

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

    public LearningGroup getLearningGroup() {
        return learningGroup;
    }

    public void setLearningGroup(LearningGroup learningGroup) {
        this.learningGroup = learningGroup;
    }

    public SopraUser getSopraUser() {
        return sopraUser;
    }

    public void setSopraUser(SopraUser sopraUser) {
        this.sopraUser = sopraUser;
    }

    public List<Comment> getCommentList() {return commentList;}

    public void setCommentList(List<Comment> commentList) {this.commentList = commentList;}

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public void addRating(Float valueOf) {
		valueOf+=valueOf;
		ratingCount++;
	}

	public float getRating() {
		return rating/ratingCount;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}



}