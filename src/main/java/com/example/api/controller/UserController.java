package com.example.api.controller;

import com.example.api.model.User;
import com.example.api.service.UserService;
import com.example.api.util.ResponseHandler;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService service = new UserService();

    @GET
    public Response getAllUsers() {
        return ResponseHandler.ok("Users fetched", service.getAllUsers());
    }

    @POST
    @Path("/add")
    public Response addUser(@Valid User user) {
        if (service.existsByEmail(user.getEmail())) {
            return ResponseHandler.badRequest("Email already exists");
        }
        service.addUser(user);
        return ResponseHandler.created("User created", user);
    }

    @PUT
    @Path("/update/{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid User user) {
        if (!service.existsById(id)) {
            return ResponseHandler.notFound("User not found");
        }
        service.updateUser(id, user);
        return ResponseHandler.ok("User updated", user);
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        if (!service.existsById(id)) {
            return ResponseHandler.notFound("User not found");
        }
        service.deleteUser(id);
        return ResponseHandler.ok("User deleted", null);
    }
}

