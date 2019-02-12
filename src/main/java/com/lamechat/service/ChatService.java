package com.lamechat.service;


import com.lamechat.model.Chat;
import com.lamechat.model.Message;
import com.lamechat.model.User;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.lamechat.service.ServiceUtils.loadFileAsString;

@Path("/")
public class ChatService {


    @GET
    @Path("/chat")
    @Produces(MediaType.TEXT_HTML)
    public Response chatPage() throws IOException {
        String str = loadFileAsString("chatpage.html");
        if (str != null) {
            return Response.ok(str).build();
        }
        return Response.status(501).build();
    }


    @GET
    @Path("/chat_styles.css")
    public Response chatStyles() throws IOException {
        String str = loadFileAsString("chat_styles.css");
        if (str != null) {
            return Response.ok(str).build();
        }
        return Response.status(501).build();
    }

    @GET
    @Path("/chat_script.js")
    public Response chatScript() throws IOException {
        String str = loadFileAsString("chat_script.js");
        if (str != null) {
            return Response.ok(str).build();
        }
        return Response.status(501).build();
    }

    @GET
    @Path("/online_users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOnlineUsers(@CookieParam("userID") String hash,@Context ContainerRequestContext requestContext) throws IOException {
        Object obj = requestContext.getProperty("user");
        List<String> users = Chat.getOnlineUsersNickname();
        if (hash != null) {
            String userNick = User.usersData.get(hash).getNickname();
            users.remove(userNick);
            users.add(0, userNick);
        }
        return Response.ok(users).build();
    }

    @POST
    @Path("/post_message")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMessage(Message message){
        int id = Chat.registerMessage(message);
        return Response.ok(id).build();
    }

    @POST
    @Path("/get_messages")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessages(String str){
        try{
            int lastid = Integer.parseInt(str);
            return Response.ok(Chat.getLastMessages(lastid)).build();
        }catch (NumberFormatException e){
            System.out.println("exc");
            return Response.ok().build();
        }

    }

}
