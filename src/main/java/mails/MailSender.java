package mails;

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

// TODO: Promote as a singleton, or service
// TODO: Test
// TODO: Implement waiting queue
/**
 * Used to send e-mail using gmail. Mail will be sent using another thread, so the call to sendMail will not be blocking
 */
public class MailSender
{
    final static Properties mailServerProperties = new Properties();

    {
        mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailServerProperties.put("mail.smtp.socketFactory.port", "465");
        mailServerProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.port", "465");
    }

    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);

    private final String mailFrom;
    private final String mailerUsername;
    private final String mailerPassword;

    @Inject
    public MailSender(@Named("mail.from") String mailFrom,
            @Named("mailer.username") String mailerUsername,
            @Named("mailer.password") String mailerPassword)
    {
        this.mailFrom = mailFrom;
        this.mailerUsername = mailerUsername;
        this.mailerPassword = mailerPassword;
    }

    /**
     * Send an e-mail to recipients
     * 
     * @param recipients
     *            List of e-mail addresses separated by a coma
     * @param title
     *            the title of the e-mail
     * @param text
     *            the content of the e-mail
     * @throws IllegalArgumentException
     *             if recipients are not parsed correctly
     */
    // TODO: Return Futur?
    public void sendMail(String recipients, String title, String text)
    {
        try
        {
            InternetAddress[] parsedRecipients = InternetAddress.parse(recipients);
            MailSenderThread mailSenderThread = new MailSenderThread(mailFrom, mailerUsername, mailerPassword);
            mailSenderThread.messagesToSend = ImmutableList.of(new MessageToSend(parsedRecipients, title, text));
            mailSenderThread.start();
        }
        catch (AddressException e)
        {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    static class MessageToSend
    {
        final InternetAddress[] recipients;
        final String title;
        final String text;

        MessageToSend(InternetAddress[] recipients, String title, String text)
        {
            this.recipients = recipients;
            this.title = title;
            this.text = text;
        }
    }

    static class MailSenderThread extends Thread
    {
        List<MessageToSend> messagesToSend;

        private final String mailFrom;
        private final String mailerUsername;
        private final String mailerPassword;

        MailSenderThread(String mailFrom, String mailerUsername, String mailerPassword)
        {
            this.mailFrom = mailFrom;
            this.mailerUsername = mailerUsername;
            this.mailerPassword = mailerPassword;
        }

        @Override
        public void run()
        {
            Session session = Session.getInstance(mailServerProperties,
                    new Authenticator()
                    {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(mailerUsername, mailerPassword);
                        }
                    });

            for (MessageToSend messageToSend : messagesToSend)
            {
                try
                {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(mailFrom));
                    message.setRecipients(Message.RecipientType.TO, messageToSend.recipients);
                    message.setSubject(messageToSend.title);
                    message.setText(messageToSend.text, "utf-8", "html");

                    Transport.send(message);

                    LOG.info("Mail (title: {}) sent to {}", messageToSend.title,
                            Joiner.on(",").join(messageToSend.recipients));
                }
                catch (MessagingException e)
                {
                    LOG.error("Mail (title: {}) send to {} failed: {}", messageToSend.title,
                            Joiner.on(",").join(messageToSend.recipients), e.getMessage());
                }

            }

        }

    }

}
