package com.example.api;

import com.example.api.controller.AuthController;
import com.example.api.controller.UserController;
import com.example.api.config.JwtAuthFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.server.validation.ValidationFeature;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App {

    public static void main(String[] args) throws Exception {

        // Jersey configuration
        ResourceConfig config = new ResourceConfig()
                .packages("com.example.api")
                .register(JacksonFeature.class)
                .register(UserController.class)
                .register(AuthController.class)
                .register(org.glassfish.jersey.server.validation.ValidationFeature.class);

        // Create Jersey Servlet
        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(config));

        // Jetty context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/api");
        context.addServlet(jerseyServlet, "/*");

        // JWT Filter applied to all endpoints except /auth/*
        FilterHolder jwtFilter = new FilterHolder(new JwtAuthFilter());
        context.addFilter(jwtFilter, "/*", EnumSet.of(DispatcherType.REQUEST));

        // Jetty server on port 8080
        Server server = new Server(8080);
        server.setHandler(context);

        // Start server
        server.start();
        System.out.println("Server running at http://localhost:8080/api");
        server.join();
    }
}

