package com.nk;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


@Path("users")
public class DetailsResorce {
    DetailsRepository repo =new DetailsRepository();
    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userSignup (UserDetails user) {
        System.out.println("getting called");
        String output = "Created customer <a href=\"customers/" +
                user.getUsername() + "\">" + user.getUsername()
                + "</a>";
        String lastVisit = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.LONG).format(new Date());
        if(repo.signup(user)){
        return Response.created(URI.create("/users/"
                + user.getUsername()))
                .entity(output)
                .cookie(new NewCookie("last-visit", lastVisit))
                .build();
    }return  Response.serverError().build();
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response userLogin (UserDetails user) {
        System.out.println("getting called");
        ;
        if(repo.login(user))
        {
            String output = " customer is  <a href=\"customers/" +
                user.getEmail() + "\">" + user.getEmail()
                + "</a>";
        String lastVisit = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.LONG).format(new Date());

        return Response.created(URI.create("/users/"
                + user.getUsername()))
                .entity(output)
                .cookie(new NewCookie("pulseId", user.getEmail()))
                .build();
    } return Response.status(Response.Status.NOT_FOUND)
                .entity("NoT FOUND, PLease signup")
                .type("text/plain").build();
    }




}
