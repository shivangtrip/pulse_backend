package com.pulse.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class PingerService implements Runnable {
    public String pingUrl;
    public PingerService(){

    }

    public PingerService(String pingUrl) {
        System.out.println("assigned");
        this.pingUrl = pingUrl;
    }

    public void run() {
        try {
            System.out.println("called thread"+this.pingUrl);
            URL url = new URL(this.pingUrl);
            url.toURI();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if (code >= 500 && code <= 511) {
                System.out.println("still down "+this.pingUrl);
                this.run();
            } else {
                EmailService emailService = new EmailService();
                emailService.sendEmail("dineshkumar.e20@gmail.com","Dinesh",this.pingUrl);
            }

        }catch(MalformedURLException e){
            System.out.println("exception "+e);
        }
        catch (URISyntaxException e) {
            System.out.println("exception "+e);
        } catch (IOException e) {
            System.out.println("exception "+e);
            this.run();
        }
    }
}
