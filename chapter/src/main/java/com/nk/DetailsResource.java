package com.nk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.Cookie.DEFAULT_VERSION;


@Path("users")
public class DetailsResource {
    DetailsRepository repo = new DetailsRepository();
    MailService mailService = new MailService();
    InputValidation inputValidation = new InputValidation();

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userSignup(UserDetails user)
    {
        if (inputValidation.isValidEmail(user.getEmail()) && inputValidation.isValidPassword(user.getPassword()) && inputValidation.isValidUsername(user.getUsername()))
        { int userId = repo.signup(user.getUsername(), user.getPassword(), user.getEmail());

        if (userId > 0) {
            int appId = repo.addAppInfo(user.getUsername());
            repo.userAppRelation(userId, appId);
            return Response.created(URI.create("/users/"
                    + user.getUsername()))
                    .entity("You have successfully created an account , please login to continue ..").status(201).type("application/json")
                    .build();
        }
        }
        return Response.status(400,"input not valid").build();
    }


    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userLogin(UserDetails user) {
        int userId = repo.login(user);
        String url = "http://localhost:8080/webapi/users/myapps";
        if (userId > 0) {
            String cookieValue = String.valueOf(repo.getCookie(userId));

            if (Integer.valueOf(cookieValue) <= 0) {
                cookieValue = String.valueOf(repo.addCookie(userId));
            }

            return Response.seeOther(URI.create(url))
                    .entity("You have successfully logged in")
                    .cookie(new NewCookie("cookieId", cookieValue,null,null,DEFAULT_VERSION, null,120,null,false,false)).build();
        }
        else {
            if (userId == -1)
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
    public Response createUser(UserDetails userDetails, @CookieParam("cookieId") int adminId) {
        if (adminId > 0) {
            if (mailService.sendMail(userDetails.email)) {
                repo.addUser(userDetails.getEmail(), adminId);
            }

            return Response.status(Response.Status.ACCEPTED).entity("added user, invite has been sent ").type("application/json").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("You are not an admin to add user")
                .type("application/json").build();
    }

    @GET
    @Path("logout")
    public Response logout(@CookieParam("cookieId") Cookie cookie) {

        if (cookie != null) {
            NewCookie newCookie = new NewCookie(cookie, null, 0, false);
            return Response.ok("You have successfully logged out").cookie(newCookie).build();
        }

        return Response.ok("OK - No session").build();

    }

    @GET
    @Path("myapps")
    public Response listApps(@CookieParam("cookieId") int cookieId) {

        int appId = repo.userApps(cookieId);
        return Response.ok("listing-apps  " + appId).build();

    }

}
