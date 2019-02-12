package com.lamechat;

import com.lamechat.model.Chat;
import com.lamechat.model.User;
import com.lamechat.service.AuthorizationFilter;
import com.lamechat.service.ChatService;
import com.lamechat.service.LoginService;
import com.lamechat.service.ServiceUtils;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.*;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args) throws Exception {

	    User user = new User("Alex","1@1.ru","123");
		User.registerUser(user);


		user = new User("Boris","2@2.ru","123");
		User.registerUser(user);

		startUsersObserver();
        startService();

	}

	private static void startUsersObserver(){
        TimerTask task = new TimerTask(){
            public void run() {
                for(User user: Chat.getOnlineUsers().stream().collect(Collectors.toList())){
                    if(user.isTimeout(5000)){
                        Chat.setOffline(user);
                    }
                }
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task,1000,1000);
    }

	private static void startService() throws Exception {
		Server server = new Server(new QueuedThreadPool(6,1));
		ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory());
		connector.setPort(8080);
		server.addConnector(connector);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
				LoginService.class.getCanonicalName()+","+ ChatService.class.getCanonicalName()+","+ AuthorizationFilter.class.getCanonicalName());

		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}


	}
}
