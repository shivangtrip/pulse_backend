package com.nk;

import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import static javax.ws.rs.core.Cookie.DEFAULT_VERSION;


@Path("users")
public class UserAuthenticationApi {

    UserDBDAO userDBDAO = new UserDBDAO();
    MailSender mailService = new MailSender();
    InputValidation inputValidation = new InputValidation();


    @POST
    @Path("signup")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userSignup(UserPOJO user) {

        boolean emailCheck = inputValidation.isValidEmail(user.getEmail());
        boolean pwdCheck = inputValidation.isValidPassword(user.getPassword());
        boolean usernameCheck = inputValidation.isValidUsername(user.getUsername());

        if (emailCheck && pwdCheck && usernameCheck) {
            int userId = userDBDAO.signup(user);

            if (userId > 0) {
                int appId =userDBDAO.createApp(user.getUsername());
                userDBDAO.userAppRelation(userId, appId);
                return Response.created(URI.create("/users/"
                        + user.getUsername()))
                        .entity("You have successfully created an account , please login to continue ..").status(201).type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }
        return Response.status(HttpServletResponse.SC_BAD_REQUEST, "input not valid").build();
    }


    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response userLogin(UserPOJO user) {

        int userId = userDBDAO.login(user);
        String url = "http://localhost:8080/webapi/users/myapps";
        if (userId > 0) {
            String cookieValue = String.valueOf(userDBDAO.getCookie(userId));

            if (Integer.valueOf(cookieValue) <= 0) {
                cookieValue = String.valueOf(userDBDAO.addCookie(userId));
            }

            return Response.seeOther(URI.create(url))
                    .entity("You have successfully logged in")
                    .cookie(new NewCookie("cookieId", cookieValue, null, null, DEFAULT_VERSION, null, 2629743, null, false, false)).build();
        } else {
            if (userId == -1)
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Email or password is incorrect")
                        .type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("email does not exists, please signup")
                .type(MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("adduser")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createUser(UserPOJO userDetails, @CookieParam("cookieId") int adminId) {
        if (adminId > 0) {
            if (mailService.sendMail(userDetails.email)) {
                userDBDAO.addUser(userDetails.getEmail(), adminId);
            }

            return Response.status(Response.Status.ACCEPTED).entity("added user, invite has been sent ").type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("You are not an admin to add user")
                .type(MediaType.APPLICATION_JSON).build();
    }


    @GET
    @Path("logout")
    public Response logout(@CookieParam("cookieId") Cookie cookie) {

        if (cookie != null) {
            int id = Integer.valueOf(cookie.getValue());

            NewCookie newCookie = new NewCookie(cookie, null, 0, false);
            userDBDAO.deleteCookie(id);
            return Response.ok("You have successfully logged out").cookie(newCookie).build();
        }

        return Response.ok("OK - No session").build();

    }


    @GET
    @Path("myapps")
    public Response listApps(@CookieParam("cookieId") int cookieId) {

        int appId = userDBDAO.appsOfUser(cookieId);
        return Response.ok("listing-apps  " + appId).build();

    }

}
