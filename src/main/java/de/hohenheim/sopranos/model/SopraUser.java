
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

    private String username;

    private String courseOfStudys;

    private String linkToPicture = "http://i.imgur.com/ppZutz0.png";

    private Integer rankpoints = 10;

    private static int deleteCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "FRIENDS",
            joinColumns = @JoinColumn(name = "FIRST"),
            inverseJoinColumns = @JoinColumn(name = "SECOND"), uniqueConstraints =
    @UniqueConstraint(columnNames = {"FIRST", "SECOND"}))
    private List<SopraUser> friendsList = new ArrayList<>();

    @ManyToMany(mappedBy = "friendsList", cascade = CascadeType.ALL)
    private List<SopraUser> friendsListTwo = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "FRIENDSREQUESTS",
            joinColumns = @JoinColumn(name = "FIRST"),
            inverseJoinColumns = @JoinColumn(name = "SECOND"), uniqueConstraints =
    @UniqueConstraint(columnNames = {"FIRST", "SECOND"}))
    private List<SopraUser> friendRequestsList = new ArrayList<>();
    @ManyToMany(mappedBy = "friendsList", cascade = CascadeType.ALL)
    private List<SopraUser> friendRequestsListTwo = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Message> sendMessageList = new ArrayList<>();
    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessageList = new ArrayList<>();

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

    @OneToMany(mappedBy = "sopraUser")
    private List<UserEvent> userEventList = new ArrayList<>();

    @OneToMany(mappedBy = "challenger")
    private List<QuizDuel> challengerList = new ArrayList<>();
    @OneToMany(mappedBy = "challenged")
    private List<QuizDuel> challengedList = new ArrayList<>();


    public List<UserEvent> getUserEventList() {
        return userEventList;
    }

    public void setUserEventList(List<UserEvent> userEventList) {
        this.userEventList = userEventList;
    }

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

    public void increaseRankpoints() {
        rankpoints++;
    }

    public void decreaseRankpoints() {
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public int getPostCount() {
        return postList.size();
    }

    public List<Question> getQuestList() {
        return questList;
    }

    public void setQuestList(List<Question> questList) {
        this.questList = questList;
    }

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

    public List<SopraUser> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<SopraUser> friendsList) {
        this.friendsList = friendsList;
    }

    public List<SopraUser> getFriendRequestsList() {
        return friendRequestsList;
    }

    public void setFriendRequestsList(List<SopraUser> friendRequestsList) {
        this.friendRequestsList = friendRequestsList;
    }

    public List<Message> getSendMessageList() {
        return sendMessageList;
    }

    public void setSendMessageList(List<Message> sendMessageList) {
        this.sendMessageList = sendMessageList;
    }

    public List<Message> getReceivedMessageList() {
        return receivedMessageList;
    }

    public void setReceivedMessageList(List<Message> receivedMessageList) {
        this.receivedMessageList = receivedMessageList;
    }

    public List<SopraUser> getFriendsListTwo() {
        return friendsListTwo;
    }

    public void setFriendsListTwo(List<SopraUser> friendsListTwo) {
        this.friendsListTwo = friendsListTwo;
    }

    public List<SopraUser> getFriendRequestsListTwo() {
        return friendRequestsListTwo;
    }

    public void setFriendRequestsListTwo(List<SopraUser> friendRequestsListTwo) {
        this.friendRequestsListTwo = friendRequestsListTwo;
    }

    public List<SopraUser> getFriendRequestsListALL() {
        ArrayList<SopraUser> s = new ArrayList<>();
        s.addAll(getFriendRequestsList());
        s.addAll(getFriendRequestsListTwo());
        return s;
    }

    public List<SopraUser> getFriendsListALL() {
        ArrayList<SopraUser> s = new ArrayList<>();
        s.addAll(getFriendsList());
        s.addAll(getFriendsListTwo());
        return s;
    }

    public List<QuizDuel> getChallengerList() {
        return challengerList;
    }

    public void setChallengerList(List<QuizDuel> challangerList) {
        this.challengerList = challangerList;
    }

    public List<QuizDuel> getChallengedList() {
        return challengedList;
    }

    public void setChallengedList(List<QuizDuel> challangedList) {
        this.challengedList = challangedList;
    }


    //TODO
    public void deleteSopraUser() {
        String deleteMail = "deleteduser" + deleteCount + "@aol.de";
        this.setEmail(deleteMail);
        this.setLearningGroups(null);
        this.setPassword("deleted");
        this.setFriendsList(null);
        this.setFriendsListTwo(null);
        this.setFriendRequestsList(null);
        this.setFriendRequestsListTwo(null);
        this.setCourseOfStudys(null);
        this.setLinkToPicture(null);



    }
}