package com.pulse.controller;


import com.pulse.payload.URLPayload;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@Path("/url")
public class UrlController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces({MediaType.APPLICATION_JSON})
    public Response getIt(ArrayList<URLPayload> urlPayload) {
        try {
            for (URLPayload u:urlPayload) {
                System.out.println(u.getUrl());
                URL url = new URL(u.getUrl());
                url.toURI();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.ok("success").build();
    }

}
