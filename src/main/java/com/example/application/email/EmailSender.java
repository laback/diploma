package com.example.application.email;

import com.example.application.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import static com.example.application.util.Constants.*;

@Component
public class EmailSender {

    @Value("${email.login}")
    private String username;

    @Value("${email.password}")
    private String password;

    private static final Properties properties;

    static {
        properties = System.getProperties();
        properties.setProperty(PROPERTY_HOST_KEY, PROPERTY_HOST_VALUE);
        properties.put(PROPERTY_AUTH_KEY, PROPERTY_AUTH_AND_STARTTLS_VALUE);
        properties.put(PROPERTY_PORT_KEY, PROPERTY_PORT_VALUE);
        properties.put(PROPERTY_STARTTLS_KEY, PROPERTY_AUTH_AND_STARTTLS_VALUE);
        properties.put(PROPERTY_FACTORY_PORT_KEY, PROPERTY_PORT_VALUE);
        properties.put(PROPERTY_FACTORY_CLASS_KEY, PROPERTY_FACTORY_CLASS_VALUE);
    }

    public void sendOrderNotif(String to, Order order) throws MessagingException {
        sendMessage(to, READY_ORDER_THEME, "Ваш заказ " + order + " готов к выдаче. Детали уточняйте у продавца");
    }

    //Отправляет письмо с данными для входа на почту
    public void sendCredentialsMessage(String to, String userLogin, String userPassword) throws MessagingException {
        sendMessage(to, CREDENTIALS_THEME, "Login: " + userLogin + "\nPassword: " + userPassword);
    }

    //Отправляет сообщение для восстановления пароля на почту
    public void sendPasswordResetLink(String to, String emailMessage) throws MessagingException {
        sendMessage(to, RESET_PASSWORD_THEME, emailMessage);
    }

    private void sendMessage(String to, String theme, String emailMessage) throws MessagingException {

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(username));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        message.setSubject(theme);

        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setText(emailMessage);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
