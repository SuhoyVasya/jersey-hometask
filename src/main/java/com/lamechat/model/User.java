package com.lamechat.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class User{

    //это вместо базы данных
    @JsonIgnore
    public static Map<String, User> usersData = new HashMap<>();

    @JsonProperty
    private String nickname;

    @JsonProperty
    private String email;

    @JsonProperty
    private String password;

    @JsonIgnore
    private long timestamp;


    public User() {
    }

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmailAddress() {
        return email;
    }

    public String getPassword(){return password;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickname, user.nickname) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, email, password);
    }

    public static String getHash(String email, String password){
        return DigestUtils.md5Hex(email + password);
    }


    public static String registerUser(User user){
        String hash = getHash(user.getEmailAddress(),user.getPassword());
        usersData.put(hash, user);
        return hash;
    }

    public void rewindTimestamp(){
        this.timestamp=System.currentTimeMillis();
    }

    public boolean isTimeout(long timeout){
        if(System.currentTimeMillis()-this.timestamp>timeout){
            return true;
        }
        return false;
    }



    public static boolean isRegistered(String hash){
        if (usersData.containsKey(hash)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {

        return "User{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
