package com.pulse.controller;

import com.pulse.payload.User;
import com.pulse.repository.AutheneticationRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
import java.util.Date;

@Path("authentication")
public class AuthenticationController {
    AutheneticationRepository autheneticationRepository = new AutheneticationRepository();

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userSignup(User user) {
        if (autheneticationRepository.signup(user)) {
            return Response.created(URI.create("/users/"
                    + user.getUsername()))
                    .entity("You have successfully created an account , please login to continue ..").status(201).type("application/json")
                    .build();
        }
        return Response.serverError().build();
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userLogin(User user) {
        System.out.println("getting called");
        int flag = autheneticationRepository.login(user);
        if (flag > 0) {
            return Response.created(URI.create("/users/"
                    + user.getUsername()))
                    .entity("You have successfully logged in")
                    .cookie(new NewCookie("pulseId", String.valueOf(flag)))
                    .build();
        } else if (flag == -1) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Email or password is incorrect")
                    .type("application/json").build();
        } else
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("email does not exists, please signup")
                    .type("application/json").build();
    }

    @POST
    @Path("adduser")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createUser(User userDetails, @CookieParam("pulseId") int adminId) {
        if (adminId > 0) {
            autheneticationRepository.addUser(userDetails.getEmail(), adminId);
            return Response.status(Response.Status.ACCEPTED).entity("added user, invite has been sent ").type("application/json").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("You are not an admin to add user")
                .type("application/json").build();

    }

    @GET
    @Path("logout")
    public Response logout(@CookieParam("pulseId") Cookie cookie) {

        if (cookie != null) {
            NewCookie newCookie = new NewCookie(cookie, null, 0, false);
            return Response.ok("You have successfully logged out").cookie(newCookie).build();
        }
        return Response.ok("OK - No session").build();

    }
}
