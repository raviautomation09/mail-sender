package com.ms.mailsender;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendInlineImagesInEmail {

	public static void Execute(String ReportFileName, String Recipients, String ImagePath, String MailSubject, String MailBody) {
		// Recipient's email ID needs to be mentioned.
		String to = "ravindra.kumar@Honeywell.com";

		// Sender's email ID needs to be mentioned
		String from = "automation@results.com";
		/*
		 * final String username = "h124795";//change accordingly final String
		 * password = "Honey@5665";//change accordingly
		 */
		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp-secure.honeywell.com";

		/*
		 * Properties props = new Properties(); props.put("mail.smtp.auth",
		 * "true"); props.put("mail.smtp.starttls.enable", "true");
		 * props.put("mail.smtp.host", host); props.put("mail.smtp.port", "25");
		 * 
		 * Session session = Session.getInstance(props, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { return new
		 * PasswordAuthentication(username, password); } });
		 */

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			StringTokenizer st4 = new StringTokenizer(Recipients, ",");
			while (st4.hasMoreElements()) {
				String actualElement = st4.nextToken();
				System.out.println("Mail Recipient:" + actualElement);
				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(actualElement));
			}

			// Set Subject: header field
			message.setSubject(MailSubject);

			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("mixed");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "<h4>"
					+ MailBody
					+ "</h4></br><img src=\"cid:image\"></br><h5>Regards,</h5><h5>Automation-Team</h5>";
			messageBodyPart.setContent(htmlText, "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);
			// second part (the image)
			messageBodyPart = new MimeBodyPart();
			// String ReportHTMLPath =
			// System.getProperty("user.dir")+File.separator+"Reports"+File.separator+ReportFileName;
			System.out.println("Image File Path:" + ImagePath);
			DataSource fds = new FileDataSource(ImagePath);
			messageBodyPart.setDataHandler(new DataHandler(fds));

/*			Path p = Paths.get(ReportFileName);
			String file = p.getFileName().toString();
			System.out.println("File Name:" + file);
*/
			// messageBodyPart.setFileName(file);
			messageBodyPart.setHeader("Content-ID", "<image>");
			// add image to the multipart
			multipart.addBodyPart(messageBodyPart);
			
			
			StringTokenizer st5 = new StringTokenizer(ReportFileName, ",");
			
				
			
			while (st5.hasMoreElements()) {
				    MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				    String fp = st5.nextToken();
				    System.out.println(fp);
				    DataSource source = new FileDataSource(fp);
				    messageBodyPart2.setDataHandler(new DataHandler(source));
				    messageBodyPart2.setFileName(source.getName());
				    multipart.addBodyPart(messageBodyPart2);
				    System.out.println(1);
				    
			}
			
/*			
			
			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			System.out.println("Report File Path:" + ReportFileName);
			DataSource source = new FileDataSource(ReportFileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(file);
			multipart.addBodyPart(messageBodyPart);*/

			// put everything together
			message.setContent(multipart);
			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Execute(args[0], args[1], args[2], args[3], args[4]);
		 /*Execute("C:\\Users\\H124795\\Desktop\\Spends_Ladakh.xlsx,C:\\Users\\H124795\\Desktop\\Spends_Ladakh.xlsx",
		 "ravindra.kumar@honeywell.com","C:\\Users\\H124795\\Desktop\\mantras.jpg",
		 "TYest", "Body");*/
	}
}