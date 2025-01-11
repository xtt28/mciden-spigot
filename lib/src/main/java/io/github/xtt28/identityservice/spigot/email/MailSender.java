package io.github.xtt28.identityservice.spigot.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailSender {

    private MailSender() {

    }

    // https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp
    public static void sendMail(@Nonnull final Session sess, @Nonnull final String from, @Nonnull final String to,
            @Nonnull final String subject,
            @Nonnull final String body) throws MessagingException, UnsupportedEncodingException {
        var msg = new MimeMessage(sess);

        msg.addHeader("Content-Type", "text/html; charset=utf-8");
        msg.addHeader("Format", "Flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(from, "IdentityService"));
        msg.setReplyTo(InternetAddress.parse(from, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        Transport.send(msg);
    }
}
