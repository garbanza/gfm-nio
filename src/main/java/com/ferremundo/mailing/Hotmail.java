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
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ferremundo.Log;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;

public class Hotmail implements Mail {
	
	
	public static void main(String[] args) {
		//send("Cotización 3WA3 : 2.97", "FERREMUNDO AGRADECE SU PREFERENCIA", new String[]{"raynmune@hotmail.com"}, new String[]{"/opt/workspace/tmp/3WA3.0","/home/dios/FERREMUNDO/pedidos/3WA3.pdf.csv"}, new String[]{"3WA3.0.pdf","3WA3.csv"});
	}
	
	public boolean send( String subject, String text,String[] recipients) {
		return send(subject, text, recipients, null, null);
	}
	public boolean send( String subject, String text,String[] recipients, String[] paths, String[] fileNames) {
		List<String> mails=new LinkedList<String>();
		Log log=new Log();
		for(String recipient : recipients){
			if(new EmailValidator().validate(recipient))mails.add(recipient);
		}
		if(mails.size()==0){
			log.info("sin recipients para correo");
			return false;
		}
		if(!new Boolean(GSettings.get("MAILING")))return true;
		try {
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.live.com");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.user", GSettings.get("EMAIL_NOTIFICATIONS"));
			props.setProperty("mail.smtp.auth", "true");

			System.out.println("Enviando…");
			// Preparamos la sesion
			Session session = Session.getInstance(props);//Session.getDefaultInstance(props);

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
			message.saveChanges();
			
			/*Transport t = session.getTransport("smtp");
			t.connect("smtp.live.com",587,GSettings.get("EMAIL_NOTIFICATIONS"), GSettings.get("EMAIL_NOTIFICATIONS_PASS"));
			t.sendMessage(message, message.getAllRecipients());
			 */
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Transport t;
					try {
						// Lo enviamos.
						t = session.getTransport("smtp");
						t.connect("smtp.live.com",587,GSettings.get("EMAIL_NOTIFICATIONS"), GSettings.get("EMAIL_NOTIFICATIONS_PASS"));
						t.sendMessage(message, message.getAllRecipients());
						// Cierre.
						t.close();
						log.object("mail sent");
					} catch (NoSuchProviderException e) {
						log.trace("failed while sending mail", e);
					} catch (MessagingException e) {
						log.trace("failed while sending mail", e);
					}
					
					
				}
			}).start();
			

			//System.out.println("Email Enviado!");
		} catch (Exception e) {
			log.trace("failed while sending mail", e);
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
