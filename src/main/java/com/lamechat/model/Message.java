package com.lamechat.model;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lamechat.serializers.MessageDeserializer;
import com.lamechat.serializers.MessageSerializer;

import java.util.Date;


@JsonSerialize(using = MessageSerializer.class)
@JsonDeserialize(using = MessageDeserializer.class)
public class Message {

    private MessageType type;
    private String text;
    private Date date;
    private User user;


    public Message() {
    }

    public Message(MessageType type, String text, Date date, User user) {
        this.type = type;
        this.text = text;
        this.date = date;
        this.user = user;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        String str = user.getNickname()+"("+date.getHours()+
                ":"+date.getMinutes()+
                ":"+date.getSeconds()+"): ";
        switch (type){
            case MESSAGE:str+=text;break;
            case USER_LOGIN:str+="log in";break;
            case USER_LOGOUT:str+="log out";break;
        }
        return str;
    }

    public static enum MessageType{
        USER_LOGIN,
        USER_LOGOUT,
        MESSAGE;
    }

}
