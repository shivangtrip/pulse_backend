package com.pulse.service;

import java.io.IOException;

import com.pulse.dbcp.DBDetailsRetrieve;
import com.pulse.dbcp.DBUrlStatusUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class PingerService implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(PingerService.class);
    public String pingUrl;
    public PingerService(){

    }

    public PingerService(String pingUrl) {
        log.info("assigned");
        this.pingUrl = pingUrl;

    }

    public void run() {
        try {
           log.info("call thread "+this.pingUrl);
            System.out.println("thread called");
            URL url = new URL(this.pingUrl);
            url.toURI();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if (code >= 500 && code <= 511) {
                log.info("still down "+this.pingUrl);
                this.run();
            } else {
                EmailService emailService = new EmailService();
                DBUrlStatusUpdate dbUrlStatusUpdate = new DBUrlStatusUpdate(this.pingUrl);
                dbUrlStatusUpdate.deleteDownStatus();
                dbUrlStatusUpdate.updateDownStatus();
                DBDetailsRetrieve dbDetailsRetrieve = new DBDetailsRetrieve(this.pingUrl);
                ArrayList<String> arr = dbDetailsRetrieve.retrieveDetails();

                emailService.sendEmail(arr.get(0),arr.get(1),this.pingUrl);
            }

        }catch(MalformedURLException e){
            log.info("exception "+e);
        }
        catch (URISyntaxException e) {
            log.info("exception "+e);
        } catch (IOException e) {
            log.info("exception "+e);
            this.run();
        }
    }
}
