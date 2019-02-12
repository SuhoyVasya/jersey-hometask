package com.lamechat.service;

import com.lamechat.model.Chat;
import com.lamechat.model.User;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URI;
import java.util.Collection;


public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        if(method.isAnnotationPresent(PermitAll.class))
        {
            return;
        }

        if(method.isAnnotationPresent(DenyAll.class))
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String hash = null;
        Collection<Cookie> cookieSet = requestContext.getCookies().values();
        for(Cookie cookie:cookieSet){
            if(!"userID".equals(cookie.getName())){
                continue;
            }
            String val = cookie.getValue();
            if(User.isRegistered(val)){
                Chat.setOnline(User.usersData.get(val));
                requestContext.setProperty("user",User.usersData.get(val));
                return;
            }
        }
        requestContext.abortWith(Response.temporaryRedirect(URI.create("/")).build());
    }
}
