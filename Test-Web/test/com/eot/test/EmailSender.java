package com.eot.test;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			sendMail("nayak.swadhin@gmail.com");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendMail(String to) throws MessagingException, AddressException
	{     
		try {
			String mailServer="smtp.gmail.com"; 
			String from ="twsbangalore@gmail.com";
			String subject="Welcome to GIM Mobile ";
			String messageBody="Welcome to GIM Mobile ... Your Web Username is admin and password is 12345678";

			// Setup mail server
			//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			Properties props = System.getProperties(); 
			props.put("mail.smtp.host", mailServer);
			//  props.put("mail.smtp.starttls.enable","true");

			props.put("mail.smtp.port", "25"); 
			props.put("mail.smtp.auth", "false");
			props.put("mail.transport.protocol", "smtp"); 
			// Get a mail session
			Session session = Session.getDefaultInstance(props, null); 
			// Define a new mail message

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);



			// Create a message part to represent the body text
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(messageBody);

			//use a MimeMultipart as we need to handle the file attachments
			Multipart multipart = new MimeMultipart();

			//add the message body to the mime message
			multipart.addBodyPart(messageBodyPart);

			// add any file attachments to the message
			//      addAtachments(attachments, multipart);

			// Put all message parts in the message
			message.setContent(multipart);

			// Send the message
			// Transport.send(message);

			Transport tr = session.getTransport("smtps");
			tr.connect(mailServer, from, "tw123456");
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
		} catch (Exception e) {

			e.printStackTrace();
		}


	}

}
