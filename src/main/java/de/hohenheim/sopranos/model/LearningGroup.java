package de.hohenheim.sopranos.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class LearningGroup {

    @Id
    @GeneratedValue
    Integer lgId;

    @Column(unique = true)
    String name;

    String description;

    String password;

    Boolean freeForAll = true;

    @OneToOne
    SopraUser sopraHost;

    @ManyToMany
    @JoinTable(
            name = "GROUPPARTICIPANTS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USERMAIL"))
    public List<SopraUser> sopraUsers = new ArrayList<>();

    @OneToMany(mappedBy = "learningGroup")
    public List<Post> postList = new ArrayList<>();

    public LearningGroup() {
    	
    }

    public Integer getLgId() {
        return lgId;
    }

    public void setLgId(Integer lgId) {
        this.lgId = lgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (!password.isEmpty())
            freeForAll = false;

        this.password = password;
    }

    public List<SopraUser> getSopraUsers() {
        return sopraUsers;
    }

    public void setSopraUsers(List<SopraUser> sopraUsers) {
        this.sopraUsers = sopraUsers;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public Boolean getFreeForAll() {
        return freeForAll;
    }

    public void setFreeForAll(Boolean freeForAll) {
        this.freeForAll = freeForAll;
    }

    public SopraUser getSopraHost() {
        return sopraHost;
    }

    public void setSopraHost(SopraUser sopraHost) {
        this.sopraHost = sopraHost;
        sopraUsers.add(sopraHost);
    }

    public int getUserCount() {
        return getSopraUsers().size();
    }
    public boolean isHost(SopraUser su){
    	if(sopraHost!=null){
    		return su.equals(sopraHost);
    	}
    	return false;
    }
}
