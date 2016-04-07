package com.ferremundo.mailing;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ferremundo.stt.GSettings;

public class HotmailSend {
	
	public static void main(String[] args) {
		//send("Cotización 3WA3 : 2.97", "FERREMUNDO AGRADECE SU PREFERENCIA", new String[]{"raynmune@hotmail.com"}, new String[]{"/opt/workspace/tmp/3WA3.0","/home/dios/FERREMUNDO/pedidos/3WA3.pdf.csv"}, new String[]{"3WA3.0.pdf","3WA3.csv"});
	}
	public static boolean send( String subject, String text,String[] recipients) {
		return send(subject, text, recipients, null, null);
	}
	public static boolean send( String subject, String text,String[] recipients, String[] paths, String[] fileNames) {
		List<String> mails=new LinkedList<String>();
		for(String recipient : recipients){
			if(new EmailValidator().validate(recipient))mails.add(recipient);
		}
		if(mails.size()==0){
			System.out.println("sin recipients para correo");
			return false;
		}
		try {
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.live.com");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.user", GSettings.get("EMAIL_NOTIFICATIONS"));
			props.setProperty("mail.smtp.auth", "true");

			System.out.println("Enviando…");
			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);

			// Construimos el mensaje
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(GSettings.get("EMAIL_NOTIFICATIONS")));
			for(String recipient : mails){
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}
			message.setSubject(subject);
			message.setText(text);
			
			Multipart multipart = new MimeMultipart();
			if(paths!=null&&fileNames!=null){
				for(int i=0;i<paths.length;i++){
					addAttachment(multipart, paths[i],fileNames[i]);
				}
			}
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(text);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			
			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect(GSettings.get("EMAIL_NOTIFICATIONS"), GSettings.get("EMAIL_NOTIFICATIONS_PASS"));
			t.sendMessage(message, message.getAllRecipients());

			// Cierre.
			t.close();

			System.out.println("Email Enviado!");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static void addAttachment(Multipart multipart, String path, String filename)
	{
	    DataSource source = new FileDataSource(path);
	    BodyPart messageBodyPart = new MimeBodyPart();
	    try {
			messageBodyPart.setDataHandler(new DataHandler(source));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			messageBodyPart.setFileName(filename);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
