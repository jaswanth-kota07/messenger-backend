package com.jashu.Messenger.model;

public class Messagedto {

    private String message;
    private int senderid;
    private int receiverid;
    private int chatid;
    public Messagedto(String message, int senderid, int receiverid, int chatid){
        this.message = message;
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.chatid = chatid;
    }

    public Messagedto() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }
}
