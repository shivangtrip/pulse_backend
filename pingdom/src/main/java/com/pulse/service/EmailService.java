package com.pulse.service;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private final static Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String email,String user, String url){
        final Properties properties = new Properties();
        InputStream inputStream;
        String propFileName = "pulse.properties";
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("EMAIL"),properties.getProperty("PASSWORD") );
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

            log.info("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean sendMail(String invitee_Mail) {

        final String username = "nandini2052@gmail.com"; // enter your mail id
        final String password = "";// enter ur password

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
            message.setFrom(new InternetAddress("nandini2052@gmail.com")); // same email id
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(invitee_Mail));// whome u have to send mails that person id
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

            return true;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
