package com.nk;

import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;



@Path("users")
public class DetailsResorce {
    DetailsRepository repo =new DetailsRepository();

    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userSignup (UserDetails user) {
        System.out.println(user.getUsername());

        if(repo.signup(user.getUsername(),user.getPassword(),user.getEmail()))
        {
          repo.addAppInfo(user.getUsername());
              repo.user_app_relation(user.getUsername());


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
        String url ="http://localhost:8080/webapi/users/myapps";
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(flag>0)
        {    if(repo.getCookie(flag)<0)
             {repo.addCookie(flag);}
             String  cookievalue= String.valueOf(repo.getCookie(flag));
             return Response.seeOther(URI.create(url))
                .entity("You have successfully logged in")
                .cookie(new NewCookie("pulseId", cookievalue)).build();
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
     public Response createUser (UserDetails userDetails, @CookieParam("pulseId") int adminId)
    {
        if (adminId>0)
        {
        repo.addUser(userDetails.getEmail(),adminId);
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

    @GET
    @Path("myapps")
    public Response  list_apps (@CookieParam("pulseId") int cookieId) {

         int app_id= repo.user_apps( cookieId);
        return Response.ok("listing-apps  "+app_id).build();

}
}
