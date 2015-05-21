package email;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hab81
 */
public class EmailNotifier {

    private static final String senderAddress = "is2731final@gmail.com";
    private static final String senderPassword = "is2731group";

    //private static String smtpStarttlsEnable = "true"; // for non-ssl connection
    private static final String smtpAuth = "true";
    private static final String smtpHost = "smtp.gmail.com";
    private static final String smtpPort = "465";
    private static final String socketFactory = "javax.net.ssl.SSLSocketFactory"; // for ssl conection

    public EmailNotifier() {
    }

    /**
     * send e-mail by SSL connection
     *
     * @param toAddress
     * @param mailSubject
     * @param mailText
     */
    public void sendMail(String toAddress, String mailSubject, String mailText) {
        Properties properties = new Properties();
        properties.put("mail.smtp.socketFactory.port", smtpPort);
        properties.put("mail.smtp.socketFactory.class", socketFactory);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderAddress, senderPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderAddress));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject(mailSubject);
            message.setText(mailText);

            Transport.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
