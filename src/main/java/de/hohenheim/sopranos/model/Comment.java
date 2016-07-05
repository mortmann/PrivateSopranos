package de.hohenheim.sopranos.model;


import javax.persistence.*;


/**
 * Created by HP Brk on 22.06.2016.
 */
@Entity
public class Comment extends DateClass{

    @Id
    @GeneratedValue
    private Integer commId;

    private String text;


    @ManyToOne
    private SopraUser sopraUser;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Post post;

    public Comment(){
    	setCreateDate();
    }

    public Integer getCommId() {
        return commId;
    }

    public void setCommId(Integer commId) {
        this.commId = commId;
    }

    public SopraUser getSopraUser() {
        return sopraUser;
    }

    public void setSopraUser(SopraUser sopraUser) {
        this.sopraUser = sopraUser;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
