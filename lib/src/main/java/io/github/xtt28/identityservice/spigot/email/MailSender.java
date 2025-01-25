package io.github.xtt28.identityservice.spigot.email;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.xtt28.identityservice.spigot.database.dto.StudentDTO;

public final class MailSender {

    private MailSender() {

    }

    private static final String ACTIVATE_SUBJECT = "Let's activate your in-game account.";
    private static final String ACTIVATE_BODY_FORMAT = """
            Hi {0} {1},

            Click on the below link to activate your account and access the BCA 2028 Lifesteal server. This link expires in 1 hour.

            {2}

            ~ IdentityService
            BCA 2028 Lifesteal Server

            If you do not recognize this email, it is safe to ignore.
            """;

    // https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp
    public static void sendMail(@Nonnull final JavaPlugin plugin, @Nonnull final Session sess, @Nonnull final String from, @Nonnull final String to,
            @Nonnull final String subject,
            @Nonnull final String body) throws MessagingException, UnsupportedEncodingException {
        var msg = new MimeMessage(sess);

        msg.addHeader("Content-Type", "text/html; charset=utf-8");
        msg.addHeader("Format", "Flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(from, "BCA 2028 Lifesteal"));
        msg.setReplyTo(InternetAddress.parse(from, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Transport.send(msg);
            } catch (final MessagingException ex) {
                plugin.getLogger().severe("Could not send message.");
                ex.printStackTrace();
            }
        });
    }

    public static void sendVerificationEmail(@Nonnull final JavaPlugin plugin, @Nonnull final Session sess, @Nonnull final String from,
            @Nonnull final String verifyIntentId, @Nonnull final StudentDTO student,
            @Nonnull final String urlTemplate) throws UnsupportedEncodingException, MessagingException {
        final var verifyUrl = MessageFormat.format(urlTemplate, verifyIntentId);
        final var body = MessageFormat.format(ACTIVATE_BODY_FORMAT, student.firstName(), student.lastName(), verifyUrl);

        sendMail(plugin, sess, from, student.email(), ACTIVATE_SUBJECT, body);
    }
}
