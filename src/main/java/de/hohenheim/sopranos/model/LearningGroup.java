package de.hohenheim.sopranos.model;

import javax.persistence.*;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LearningGroup {

    @Id
    @GeneratedValue
    private Integer lgId;

    @Column(unique = true)
    private String name;

    private String description;

    private  String password;

    private boolean freeForAll = true;
    private boolean freeForFriends = false;

    private Date createDate;


    @OneToOne
    private SopraUser sopraHost;

    @OneToMany(mappedBy = "released")
    private  List<Question> questList = new ArrayList<>();
    @OneToMany(mappedBy = "notReleased")
    private  List<Question> notReleasedQuestionList = new ArrayList<>();

    @OneToMany(mappedBy = "learningGroup")
    private  List<Post> postList = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "GROUPPARTICIPANTS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USERMAIL"))
    private  List<SopraUser> sopraUsers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "BLACKLIST",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "BLACK_USER"))
    private  List<SopraUser> blackList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "GRAYLIST",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "BLACK_USER"))
    private  List<SopraUser> grayList = new ArrayList<>();




    public LearningGroup() {}

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
        if (password == null || password.isEmpty()){
        	freeForAll = true;
        } else {
            freeForAll = false;
        }
        this.password = password;
    }

    public List<SopraUser> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<SopraUser> blackList) {
        this.blackList = blackList;
    }

    public List<SopraUser> getGrayList() {
        return grayList;
    }

    public void setGrayList(List<SopraUser> grayList) {
        this.grayList = grayList;
    }

    public List<SopraUser> getSopraUsers() {
        return sopraUsers;
    }

    public void setSopraUsers(List<SopraUser> sopraUsers) {
        this.sopraUsers = sopraUsers;
    }

    public Integer getLgId() {return lgId;}

    public void setLgId(Integer lgId) {this.lgId = lgId;}

    public List<Question> getQuestList() {return questList;}

    public void setQuestList(List<Question> questList) {this.questList = questList;}

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

    public boolean isHost(SopraUser su) {
        if (sopraHost != null) {
            return su.equals(sopraHost);
        }
        return false;
    }

    public void banSopraUser(SopraUser user) {
        if (sopraUsers.contains(user)) {
            sopraUsers.remove(user);
            user.getLearningGroups().remove(this);
            blackList.add(user);
            user.getBlack().add(this);
        }
        if (grayList.contains(user)) {
            grayList.remove(user);
            user.getGray().remove(this);
        }
    }

    public void unbanSopraUser(SopraUser user) {
        if (blackList.contains(user)) {
            blackList.remove(user);
            user.getLearningGroups().remove(this);
            user.getBlack().remove(this);
        }
    }

    public void lockSopraUser(SopraUser user) {
        if (sopraUsers.contains(user)) {
            grayList.add(user);
            user.getGray().add(this);
        }
    }

    public void unlockSopraUser(SopraUser user) {
        if (grayList.contains(user)) {
//            sopraUsers.add(user);
            grayList.remove(user);
            user.getGray().remove(this);
        }
    }
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate() {
		this.createDate = new Date(System.currentTimeMillis());
	}
	public String getCreateDateString() {
		return DateFormat.getInstance().format(getCreateDate());
	}

	public List<Question> getNotReleasedQuestionList() {
		return notReleasedQuestionList;
	}

	public void setNotReleasedQuestionList(List<Question> notReleasedQuestionList) {
		this.notReleasedQuestionList = notReleasedQuestionList;
	}

	public boolean isFreeForFriends() {
		return freeForFriends;
	}

	public void setFreeForFriends(boolean freeForFriends) {
		this.freeForFriends = freeForFriends;
	}
}
