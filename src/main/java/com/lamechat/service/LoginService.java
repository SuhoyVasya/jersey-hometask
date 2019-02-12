package com.lamechat.service;


import com.lamechat.model.User;
import com.lamechat.model.AuthStatus;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.*;

import static com.lamechat.service.ServiceUtils.loadFileAsByteArray;
import static com.lamechat.service.ServiceUtils.loadFileAsString;


@Path("/")
public class LoginService {
    static String path = "./src/main/resources/";

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public Response mainPage(@CookieParam("userID") String hash,
                             @Context HttpServletResponse servletResponse, @Context ContainerRequestContext requestContext) throws IOException {
        if (User.isRegistered(hash)) {
            servletResponse.sendRedirect("/chat");
        }
        String str = loadFileAsString("login.html");
        if(str!=null){
            return Response.ok(str).build();
        }
        return Response.status(501).build();

    }

    @GET
    @PermitAll
    @Path("/login.js")
    public Response getLoginJS(){
        String str = loadFileAsString("login.js");
        if(str!=null){
            return Response.ok(str).build();
        }
        return Response.status(500).build();
    }


    @GET
    @PermitAll
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public Response register(){
        String str = loadFileAsString("register.html");
        if(str!=null){
            return Response.ok(str).build();
        }
        return Response.status(500).build();
    }

    @GET
    @PermitAll
    @Path("/register.js")
    public Response getRegisterJS(){
        String str = loadFileAsString("register.js");
        if(str!=null){
            return Response.ok(str).build();
        }
        return Response.status(500).build();
    }

    @GET
    @PermitAll
    @Path("/styles.css")
    public Response getCss() throws IOException {
        String str = loadFileAsString("styles.css");
        if(str!=null){
            return Response.ok(str).build();
        }
        return Response.status(500).build();
    }

    @GET
    @PermitAll
    @Path("/letter.png")
    public Response getPng() throws IOException {
        byte[] arr = loadFileAsByteArray("letter.png");
        if(arr!=null){
            return Response.ok(arr).build();
        }
        return Response.status(500).build();
    }


    @POST
    @PermitAll
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register_post(User user) {
        String nick = user.getNickname();
        String email = user.getEmailAddress().toLowerCase();
        if ((nick == null) || (email == null)) {
            return Response.ok(AuthStatus.WRONG_EMAIL).build();
        }

        for (User user1 : User.usersData.values()) {
            if (user1.getNickname().equals(user.getNickname())) {
                return Response.ok(AuthStatus.NICK_IS_ALREADY_IN_USE).build();
            }
            if (user1.getEmailAddress().equals(user.getEmailAddress())) {
                return Response.ok(AuthStatus.EMAIL_IS_ALREADY_IN_USE).build();
            }
        }
        System.out.println("registered :"+user);
        String hash = User.registerUser(user);
        NewCookie cookie = new NewCookie("userID", hash);
        return Response.ok(AuthStatus.AUTHORIZED).cookie(cookie).build();
    }


    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login_post(User user,
                               @Context HttpServletResponse servletResponse) throws IOException {
        String password = user.getNickname();
        String email = user.getEmailAddress().toLowerCase();
        if (password == null) {
            return Response.ok(AuthStatus.WRONG_PASSWORD).build();
        }
        if (email == null) {
            return Response.ok(AuthStatus.WRONG_EMAIL).build();
        }
        for (User user1 : User.usersData.values()) {
            if (user1.getEmailAddress().equals(user.getEmailAddress())) {
                if (user1.getPassword().equals(user.getPassword())) {
                    System.out.println("login: "+user1);
                    String hash = User.getHash(user.getEmailAddress(), user.getPassword());
                    NewCookie cookie = new NewCookie("userID", hash);
                    return Response.ok(AuthStatus.AUTHORIZED).cookie(cookie).build();

                } else {
                    return Response.ok(AuthStatus.WRONG_PASSWORD).build();
                }
            }
        }
        return Response.ok(AuthStatus.WRONG_EMAIL).build();
    }

}
