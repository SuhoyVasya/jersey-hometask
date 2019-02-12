package com.lamechat.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class Chat {
    private static Set<User> onlineUsers = new TreeSet<User>(new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return o1.getNickname().compareTo(o2.getNickname());
        }
    });

    private static List<Message> chat = new CopyOnWriteArrayList<>();


    public static void setOnline(User user){
        if(!onlineUsers.contains(user)){
            onlineUsers.add(user);
            registerMessage(new Message(Message.MessageType.MESSAGE.USER_LOGIN,"",new Date(),user));
        }
        user.rewindTimestamp();
    }

    public static void setOffline(User user){
        if(onlineUsers.contains(user)) {
            onlineUsers.remove(user);
            registerMessage(new Message(Message.MessageType.MESSAGE.USER_LOGOUT,"",new Date(),user));
        }
    }

    public static List<String> getOnlineUsersNickname(){
        return onlineUsers.stream().map(user -> user.getNickname()).collect(Collectors.toList());
    }

    public static Set<User> getOnlineUsers() {
        return onlineUsers;
    }

    public static  int registerMessage(Message message){
        chat.add(message);
        return chat.size();
    }

    public static List<Message> getLastMessages(int lastid){
        if(lastid>chat.size()){
            return chat.subList(0,0);
        }
        if(chat.size()-lastid>100)
        {
            lastid = chat.size()-100;
        }
        return chat.subList(lastid,chat.size());
    }


}
