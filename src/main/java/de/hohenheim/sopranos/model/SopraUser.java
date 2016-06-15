
package de.hohenheim.sopranos.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SopraUser {

    @Id
    @Column(name = "email", unique = true, nullable = false)
    String email;

    String password;

    String name;

    @Column(unique = true)
    String username;

    String courseOfStudys;

    Integer rankpoints;



    @ManyToMany(mappedBy = "sopraUsers")
    public List<LearningGroup> learningGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "blackList")
    public List<LearningGroup> black = new ArrayList<>();

    @ManyToMany(mappedBy = "grayList")
    public List<LearningGroup> gray = new ArrayList<>();

    @OneToMany(mappedBy = "sopraUser")
    public List<Post> postList = new ArrayList<>();

    public SopraUser() {
    }

    public SopraUser(String email, String password) {
        this.email = email;
        this.password = password;
        this.name = null;
        this.courseOfStudys = null;
        this.rankpoints = 0;
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

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
    public int getPostCount(){
    	return postList.size();
    }

}