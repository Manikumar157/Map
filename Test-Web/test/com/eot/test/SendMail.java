package com.eot.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	private String from;
	private String to;
	private String subject;
	private String text;

	public SendMail(String from, String to, String subject, String text){
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.text = text;
	}

	public void send(){

		String mailServer="smtp.gmail.com"; 
		
		Properties props = new Properties();
		props.put("mail.smtp.host", mailServer);
		//  props.put("mail.smtp.starttls.enable","true");

		props.put("mail.smtp.port", "25"); 
		props.put("mail.smtp.auth", "false");
		props.put("mail.transport.protocol", "smtp"); 

		Session mailSession = Session.getDefaultInstance(props);
		Message simpleMessage = new MimeMessage(mailSession);

		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		try {
			fromAddress = new InternetAddress(from);
			toAddress = new InternetAddress(to);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			simpleMessage.setSubject(subject);
			simpleMessage.setText(text);

			System.out.println("Before Successfully send");

			Transport.send(simpleMessage);

			System.out.println("Successfully sent");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String from = "support@easoftech.com";
		String to = "sudhanshu@nhancenow.com";
		String subject = "Test";
		String message = "A test message";

		SendMail sendMail = new SendMail(from, to, subject, message);
		sendMail.send();
	}

}