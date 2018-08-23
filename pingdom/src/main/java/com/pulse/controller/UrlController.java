package com.pulse.controller;


import com.pulse.repository.UrlsRepository;
import com.pulse.dbcp.UrlDBInsert;
import com.pulse.payload.URLPayload;
import com.pulse.service.PingerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@Path("/url")
public class UrlController {
    private final static Logger log = LoggerFactory.getLogger(UrlController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getIt(ArrayList<URLPayload> urlPayload) {
        boolean flag = true;
        for (URLPayload u : urlPayload) {
            log.info(u.getUrl());
            try {
                URL url = new URL(u.getUrl());
                url.toURI();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int code = connection.getResponseCode();
                if (code >= 500 && code <= 511) {
                    u.setStatus("down");
                    log.info("website down");
                    Thread thread = new Thread(new PingerService(u.getUrl()));
                    thread.start();
                } else {
                    u.setStatus("up");
                }

            }catch(MalformedURLException e){
                flag = false;
                u.setStatus("Invalid Url");
            }
            catch (URISyntaxException e) {
                flag = false;
                u.setStatus("Invalid Url");
            } catch (IOException e) {
                u.setStatus("down");
                log.info("website down1");
                Thread thread = new Thread(new PingerService(u.getUrl()));
                thread.start();

            }
        }
        //Initiating database pooling
        UrlDBInsert dbInsert = new UrlDBInsert(urlPayload);
        dbInsert.insertURLSList();
        if (flag) {
            return Response.ok(urlPayload).build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(urlPayload).build();
        }

    }

}
