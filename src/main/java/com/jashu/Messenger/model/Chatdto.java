package com.jashu.Messenger.model;

public class Chatdto {


    private int chatid;
    private String otheruser;
    private int otheruserid;

    public Chatdto() {
    }

    public Chatdto(int chatid, String otheruser, int otheruserid) {
        this.chatid = chatid;
        this.otheruser = otheruser;
        this.otheruserid = otheruserid;
    }

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    public String getOtheruser() {
        return otheruser;
    }

    public void setOtheruser(String otheruser) {
        this.otheruser = otheruser;
    }

    public int getOtheruserid() {
        return otheruserid;
    }

    public void setOtheruserid(int otheruserid) {
        this.otheruserid = otheruserid;
    }
}
