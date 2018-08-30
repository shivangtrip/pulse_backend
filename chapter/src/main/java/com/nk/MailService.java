package com.nk;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {


    public boolean sendMail(String invitee_Mail) {

        final String username = Constants.EMAIL; // enter your mail id
        final String password = Constants.PASSWORD;// enter ur password

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
            message.setFrom(new InternetAddress(Constants.EMAIL)); // same email id
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
