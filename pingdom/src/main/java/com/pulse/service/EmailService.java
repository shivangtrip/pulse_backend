package com.pulse.service;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    public void sendEmail(String email,String user, String url){
        final String username = "dineshkumar.e20@gmail.com"; // enter your mail id
        final String password = "Elumalai20";// enter ur password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("dineshkumar.e20@gmail.com")); // same email id
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));// whome u have to send mails that person id
            message.setSubject("Pulse Status");
            message.setText("Dear "+user+","
                    + "\n\n The Url "+url+" which you entered is working now. Checkout");
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
