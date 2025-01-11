package io.github.xtt28.identityservice.spigot.email;

import java.util.Properties;

import javax.annotation.Nonnull;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.bukkit.configuration.file.FileConfiguration;

public final class SessionCreator {
    
    private SessionCreator() {

    }

    // https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp
    public static Session createSessionFromConfig(@Nonnull final FileConfiguration config) {
        final var user = config.getString("mail.user");
        final var auth = config.getBoolean("mail.auth", false);
        final var pass = config.getString("mail.password");
        final var host = config.getString("mail.host");
        final var port = config.getInt("mail.port", 587);
        final var starttls = config.getBoolean("mail.starttls", false);

        final var props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);

        if (auth) {
            var authenticator = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            };

            return Session.getInstance(props, authenticator);
        }

        return Session.getInstance(props);
    }
}
