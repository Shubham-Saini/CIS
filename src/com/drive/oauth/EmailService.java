package com.drive.oauth;
import java.util.Date;
import java.util.Properties;
 
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService 
{
	private static String host = "smtp.gmail.com";
	private static String port = "587";
	private static String userName = "cis.helpcenter@gmail.com";
	private static String password = "password";
	private static String toAddress ="cis.helpcenter@gmail.com";
	public static boolean sendEmail(String subject, String message,String receiver)
	{
		boolean result = false;
        // sets SMTP server properties
		try
		{
	        Properties properties = new Properties();
	        properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", port);
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	 
	        // creates a new session with an authenticator
	        Authenticator auth = new Authenticator() {
	            public PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(userName, password);
	            }
	        };
	        if(receiver!=null)
	        {
	        	toAddress = receiver;
	        }
	        Session session = Session.getInstance(properties, auth);
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(userName));
	        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        StringBuilder resp = null;
	        if(subject.equals("recover"))
	        {
	        	msg.setSubject("Account Verification");
	        	resp = new StringBuilder("This email is in response to your request to recover your account<br>.");
	            resp.append("Please click on the below link to verify the account<br> ");
	            resp.append("http://localhost:8080/DriveProject/ValidHandler?caller=account_verification&token="+message);
	            resp.append(" <br> If this request is not initiated by you than contact our help desk at ");
	            resp.append("cis.helpcenter@gmail.com");
	            msg.setText(resp.toString());
	            
	        }
	        else
	        {
	        	msg.setSubject(subject);
	        	msg.setText(message);
	        }
	        msg.setSentDate(new Date());
	        Transport.send(msg);
	        System.out.println("Feedback sent ");
	        result = true;
		}
		catch(Exception e)
		{
			System.out.println("Trouble sending email in sendEmail");
			e.printStackTrace();
		}
		return result;
    }
}
