package edu.ben.rate_review.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Allows user to send an email
 * 
 * @author Mike
 * @version 1-29-2017
 */
public class Email {

	public static void deliverEmail(String first_name, String email, String emailSubject, String emailBody) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				// create email account and link here
				return new PasswordAuthentication("ratereviewsite", "capstone");
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject(emailSubject);
			message.setText(emailBody);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
