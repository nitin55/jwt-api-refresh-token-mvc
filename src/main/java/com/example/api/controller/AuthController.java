package com.example.api.controller;

import com.example.api.service.AuthService;
import com.example.api.util.ResponseHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private final AuthService service = new AuthService();

    @POST
    @Path("/login")
    public Response login(Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
      
        Map<String, String> tokens = service.login(username,password);
        if (tokens == null) return ResponseHandler.unauthorized("Invalid credentials");
        return ResponseHandler.ok("Login successful", tokens);
        
        
    }

    @POST
    @Path("/refresh")
    public Response refresh(Map<String, String> req) {
        String refreshToken = req.get("refreshToken");
        Map<String, String> tokens = service.refresh(refreshToken);
        if (tokens == null) return ResponseHandler.unauthorized("Invalid refresh token");
        return ResponseHandler.ok("Token refreshed", tokens);
    }
}

