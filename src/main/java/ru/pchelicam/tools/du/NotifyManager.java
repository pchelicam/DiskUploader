package ru.pchelicam.tools.du;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Sends email to user
 */
public class NotifyManager {
    public static void notifications(String subject, String messageBody) throws Exception {
        MailSettings mailSettings = MailSettingsValidator.loadSettings();
        if (mailSettings != null) {
            String to = mailSettings.getEmail();
            String from = mailSettings.getEmail();
            final String username = mailSettings.getUsername();
            final String password = mailSettings.getPassword();
            String host = mailSettings.getHost();

            Properties props = new Properties();
            props.put("mail.smtp.auth", mailSettings.getAuth());
            props.put("mail.smtp.starttls.enable", mailSettings.getStarttls_enable());
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", mailSettings.getPort());
            props.put("mail.smtp.encryption", mailSettings.getEncryption());

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(messageBody);
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        else{

        }
    }
}
