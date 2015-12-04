package com.gazette.app.model.chat;

public class Message {
    private String msgID;
    private String fromName, message;
    private boolean isSelf;
    private String time;
    private boolean isDelivered = false;

    public Message() {
    }

    public Message(String fromName, String message,String time, boolean isSelf) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
        this.time = time;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }
}
