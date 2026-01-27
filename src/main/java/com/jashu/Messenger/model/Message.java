package com.jashu.Messenger.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    private int chatid;
    private int senderid;
    private int receiverid;

    public Message(String message, int chatid, int senderid, int receiverid) {
        this.message = message;
        this.chatid = chatid;
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChatid() {
        return chatid;
    }

    public void setChaid(int chaid) {
        this.chatid = chaid;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    public int getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(int receiverid) {
        this.receiverid = receiverid;
    }

    public Message() {
    }
}
