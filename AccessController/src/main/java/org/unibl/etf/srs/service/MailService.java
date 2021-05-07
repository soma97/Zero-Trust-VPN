package org.unibl.etf.srs.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailService {

    public static void sendEmail(List<String> recipients, String title, String content)
    {
        final String username = "milos.ip.test@gmail.com";
        final String password = "glicerin1";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        StringBuilder recipientsString = new StringBuilder();
        for(String recipient : recipients) {
            recipientsString.append(recipient).append(",");
        }

        recipientsString = new StringBuilder(recipientsString.substring(0, recipientsString.length() - 1));

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "ZT - VPN"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientsString.toString()));
            message.setSubject(title);
            message.setText(content);

            Transport.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}