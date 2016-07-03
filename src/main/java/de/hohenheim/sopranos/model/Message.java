package de.hohenheim.sopranos.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

@Entity
public class Message extends DateClass {

    @Id
    @GeneratedValue
    Integer id;

    @Column(length = 10000)
    String message;

    @ManyToOne
    SopraUser sender;

    @ManyToOne
    SopraUser receiver;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SopraUser getSender() {
        return sender;
    }

    public void setSender(SopraUser sender) {
        this.sender = sender;
    }

    public SopraUser getReceiver() {
        return receiver;
    }

    public void setReceiver(SopraUser receiver) {
        this.receiver = receiver;
    }

    public static List<String> splitString(String rec) {

        rec = rec.toLowerCase();
        rec = rec.replaceAll("\\s", "");
        List<String> recz = Arrays.asList(rec.split(","));
        return recz;

    }
}
