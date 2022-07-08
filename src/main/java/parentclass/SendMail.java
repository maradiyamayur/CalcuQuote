package parentclass;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import utility.UtilityMethods;

public class SendMail extends BaseInit {

	public static void execute(Multipart multipart, String fileName) throws Exception {

		final String username = prop.getProperty("EMAIL_ID");
		final String password = prop.getProperty("EMAIL_PASSWORD");
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(prop.getProperty("EMAIL_ID")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(prop.getProperty("EMAIL_ID")));
			message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse("payal.nanavati@triveniglobalsoft.com"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String currentdate = sdf.format(new Date());
			message.setSubject("CalcuQuote Automation Report on ".concat(getInstance()).concat(" instance Dated ")
					.concat(currentdate));
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(fileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			// messageBodyPart.setFileName(fileName);
			messageBodyPart.setFileName(new File(fileName).getName());

			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			System.out.println("~>> EMAIL SENDING");
			Transport.send(message);
			System.out.println("~>> EMAIL SENDING DONE");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getInstance() {
		String strInstance = null;
		try {
			if (UtilityMethods.url.contains("app.calcuquote.com/SearchCQStaging/")
					|| UtilityMethods.url.contains("app.calcuquote.com/stagingsandbox/")) {
				strInstance = "App Staging";

			} else if (UtilityMethods.url.contains("app.calcuquote.com/Regression/")) {
				strInstance = "APP Regression";
			} else if (UtilityMethods.url.contains("app.calcuquote.com/Support/")) {
				strInstance = "APP Support";
			} else if (UtilityMethods.url.contains("qa.calcuquote.com/qa1")) {
				strInstance = "QA1";
			} else if (UtilityMethods.url.contains("qa.calcuquote.com/QAIdentity/")) {
				strInstance = "QA Identity";
			} else if (UtilityMethods.url.contains("qa.calcuquote.com/qa3")) {
				strInstance = "QA3";
			}
		} catch (NullPointerException e) {

		}
		return strInstance;
	}
}
