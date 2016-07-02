
package de.hohenheim.sopranos.model;

import javax.persistence.*;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SopraUser {

    @Id
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String password;

    private String name;
    private Date createDate;
    @Column(unique = true)
    private String username;
    
    private String courseOfStudys;

    private String linkToPicture;
    
    private Integer rankpoints = 10;

    @ManyToMany(mappedBy = "sopraUsers")
    private List<LearningGroup> learningGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "blackList")
    private List<LearningGroup> black = new ArrayList<>();

    @ManyToMany(mappedBy = "grayList")
    private List<LearningGroup> gray = new ArrayList<>();

    @OneToMany(mappedBy = "sopraUser")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "sopraUser")
    private List<Question> questList = new ArrayList<>();

    @OneToMany(mappedBy = "sopraUser")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "generated")
    private List<Quiz> quizList = new ArrayList<>(); 
     
    public SopraUser() {
    	createDate = new Date(System.currentTimeMillis());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseOfStudys() {
        return courseOfStudys;
    }

    public void setCourseOfStudys(String courseOfStudys) {
        this.courseOfStudys = courseOfStudys;
    }

    public Integer getRankpoints() {
        return rankpoints;
    }
    public void increaseRankpoints(){
    	rankpoints++;
    }
	public void decreaseRankpoints(){
		rankpoints--;
	}
    public void setRankpoints(Integer rankpoints) {
        this.rankpoints = rankpoints;
    }

    public List<LearningGroup> getLearningGroups() {
        return learningGroups;
    }

    public void setLearningGroups(List<LearningGroup> learningGroups) {
        this.learningGroups = learningGroups;
    }

    public List<LearningGroup> getBlack() {
        return black;
    }

    public void setBlack(List<LearningGroup> black) {
        this.black = black;
    }

    public List<LearningGroup> getGray() {
        return gray;
    }

    public void setGray(List<LearningGroup> gray) {
        this.gray = gray;
    }

    public List<Comment> getCommentList() {return commentList;}

    public void setCommentList(List<Comment> commentList) {this.commentList = commentList;}

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public int getPostCount() {
        return postList.size();
    }

    public List<Question> getQuestList() {return questList;}

    public void setQuestList(List<Question> questList) {this.questList = questList;}

	public Date getCreateDate() {
		return createDate;
	}

	public List<Quiz> getQuizList() {
		return quizList;
	}

	public void setQuizList(List<Quiz> quizList) {
		this.quizList = quizList;
	}

	public void setCreateDate() {
		this.createDate = new Date(System.currentTimeMillis());
	}
	public String getCreateDateString() {
		return DateFormat.getInstance().format(getCreateDate());
	}

	public String getLinkToPicture() {
		return linkToPicture;
	}

	public void setLinkToPicture(String linkToPicture) {
		this.linkToPicture = linkToPicture;
	}
}