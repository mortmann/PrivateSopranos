package de.hohenheim.sopranos.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
@Entity
public class Message extends DateClass{
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	private SopraUser sender;
	
	@ManyToMany
	@JoinTable(
            name = "MESSAGERECEIVERS",
            joinColumns = @JoinColumn(name = "MESSAGE_ID"),
            inverseJoinColumns = @JoinColumn(name = "RECEIVER_ID"))
	private List<SopraUser> receivers = new ArrayList<>();
	
    @Column(length = 10000000)
    private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SopraUser getSender() {
		return sender;
	}

	public void setSender(SopraUser sender) {
		this.sender = sender;
	}

	public List<SopraUser> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<SopraUser> receivers) {
		this.receivers = receivers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
