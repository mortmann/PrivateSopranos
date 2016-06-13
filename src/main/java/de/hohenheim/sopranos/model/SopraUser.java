
package de.hohenheim.sopranos.model;

import javax.persistence.*;
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
    public List<LearningGroup> learningGroups;


    @OneToMany(mappedBy = "sopraUser")
    public List<Post> postSet;

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

    public List<Post> getPostSet() {
        return postSet;
    }

    public void setPostSet(List<Post> postSet) {
        this.postSet = postSet;
    }
}