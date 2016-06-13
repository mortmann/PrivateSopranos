package de.hohenheim.sopranos.model;

import javax.persistence.*;
import java.util.List;


@Entity
public class LearningGroup {

    @Id
    @GeneratedValue
    Integer lgId;

   // @Column(unique = true)
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
    public List<SopraUser> sopraUsers;

    @OneToMany(mappedBy = "learningGroup")
    public List<Post> postSet;


    public LearningGroup() {
    }

    public LearningGroup(SopraUser sopraHost, String name) {
        this.sopraHost = sopraHost;
        this.name = name;
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

    public List<Post> getPostSet() {
        return postSet;
    }

    public void setPostSet(List<Post> postSet) {
        this.postSet = postSet;
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
    }

    public int getUserCount(){
        return 1;
                //getSopraUsers().size();
    }
}
