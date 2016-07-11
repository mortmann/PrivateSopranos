package de.hohenheim.sopranos.model;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burakhan on 07.06.2016.
 */
@Transactional
@Entity
public class Post extends DateClass{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 10000)
    private String text;

    private String heading;

    private float rating=0;
    private int ratingCount=0;
    
    private File file;
    @Column(length = 10000000)
    private byte[] filedata;
    @ManyToOne
    private LearningGroup learningGroup;

    @ManyToOne
    private SopraUser sopraUser;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public Post(){
    	setCreateDate();
    }


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
    	setCreateDate();
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

	public File getFile() {
		if(file == null ){
			return null;
		}
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file.getPath());
			fos.write(filedata);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		try {
			filedata = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}

	public void addRating(Float valueOf) {
		valueOf+=valueOf;
		ratingCount++;
	}

	public float getRating() {
		if(rating==0 || ratingCount==0){
			return 0;
		}
		return rating/ratingCount;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public byte[] getFiledata() {
		return filedata;
	}

	public void setFiledata(byte[] filedata) {
		this.filedata = filedata;
	}



}