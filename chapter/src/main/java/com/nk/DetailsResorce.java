package com.nk;

import java.net.URI;
import javax.ws.rs.*;
import javax.ws.rs.core.*;



@Path("users")
public class DetailsResorce {
    DetailsRepository repo =new DetailsRepository();

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userSignup (UserDetails user) {
        if(repo.signup(user))
        {
        return Response.created(URI.create("/users/"
                + user.getUsername()))
                .entity("You have successfully created an account , please login to continue ..").status(201).type("application/json")
                .build();
        }
    return  Response.serverError().build();
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userLogin (UserDetails user) {
        System.out.println("getting called");
      int flag=repo.login(user);
        if(flag>0)
        {
        return Response.created(URI.create("/users/"
                + user.getUsername()))
                .entity("You have successfully logged in")
                .cookie(new NewCookie("pulseId", String.valueOf(flag)))
                .build();
        }
        else{
             if (flag== -1)
                 return Response.status(Response.Status.UNAUTHORIZED)
                         .entity("Email or password is incorrect")
                         .type("application/json").build();
        }
    return Response.status(Response.Status.NOT_FOUND)
                .entity("email does not exists, please signup")
                .type("application/json").build();
    }

    @POST
    @Path("adduser")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
     public Response createUser (String email, @CookieParam("pulseId") int adminId)
    {
        if (adminId>0)
        {
        repo.addUser(email,adminId);
        return Response.status(Response.Status.ACCEPTED).entity("added user, invite has been sent ").type("application/json").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("You are not an admin to add user")
                .type("application/json").build();

    }

    @GET
    @Path("logout")
    public Response logout (@CookieParam("pulseId") Cookie cookie) {

        if (cookie != null) {
            NewCookie newCookie = new NewCookie(cookie,null, 0, false);
            return Response.ok("You have successfully logged out").cookie(newCookie).build();
        }

        return Response.ok("OK - No session").build();

    }
}
