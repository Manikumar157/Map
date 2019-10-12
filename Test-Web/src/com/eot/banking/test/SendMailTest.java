package com.eot.banking.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTest {public static void main(String[] args) {

    final String username = "support@easoftech.com";
    final String password = "KktFZB9wCXY3";

    Properties prop = new Properties();
	prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "465");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.socketFactory.port", "465");
    prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    
    Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

    try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("Mobile-Wallet"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("mohammedali.shaikh@easoftech.com")
        );
        message.setSubject("Testing Mail");
        message.setText("Dear Ali,"
                + "\n\n This is test mail, please do not spam this email!");

        Transport.send(message);

        System.out.println("Done");

    } catch (MessagingException e) {
        e.printStackTrace();
    }
}
}
