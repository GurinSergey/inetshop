package com.webstore.core.config;

import com.webstore.core.service.MailSender;
import com.webstore.core.service.impl.IMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Created by MZlenko on 29.07.2016.
 */

@Configuration
@PropertySource("classpath:property/mail.properties")
@ComponentScan("com.webstore.core")
public class MailConfig {
    @Resource
    private Environment environment;


    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", environment.getProperty("smtp.auth"));
        properties.put("mail.smtp.starttls.enable", environment.getProperty("smtp.starttls.enable"));
        properties.put("mail.smtp.host", environment.getProperty("smtp.host"));
        properties.put("mail.smtp.port", environment.getProperty("smtp.port"));
        properties.put("mailusername", environment.getProperty("mailusername"));
        /*LAO!*/
 /*Использовать только для нашего прокси рсталовского!!!!!!!!!!!!!!!!*/
        properties.setProperty("proxySet","true");
        properties.setProperty("socksProxyHost","192.168.100.109");
        properties.setProperty("socksProxyPort","8080");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
/*************************************************************************/
        return properties;
    }

    private Session getSession() {
        return Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(environment.getProperty("mailusername"), environment.getProperty("mailpassword"));
                    }
                });
    }

    @Bean
    public MailSender mailSender() {
        IMailSender mailSender = new IMailSender();
        mailSender.setSession(getSession());
        return mailSender;
    }
}
