package com.nk;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
        if(repo.signup(user))
        {
        return Response.created(URI.create("/users/"
                + user.getUsername()))
                .entity(output)
                .cookie(new NewCookie("lastVisit",lastVisit))
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
    return Response.status(Response.Status.NOT_FOUND)
                .entity("NOT FOUND, Please signup")
                .type("text/plain").build();
    }

    @POST
    @Path("adduser")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
     public Response createUser (String email, @CookieParam("pulseId") int adminId)
    {

        System.out.println(adminId);
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
//        logCookie = 0;
//        return Response.status(Response.Status.REQUEST_TIMEOUT)  .entity("U have logged out").cookie(new NewCookie("pulseId",String.valueOf(logCookie))).type("text/plain").build();
            
        if (cookie != null) {
            NewCookie newCookie = new NewCookie(cookie,null, 0, false);
            return Response.ok("OK").cookie(newCookie).build();
        }

        return Response.ok("OK - No session").build();

    }
}
