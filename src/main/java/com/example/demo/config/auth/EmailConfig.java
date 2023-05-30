package com.example.demo.config.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
@Component
@PropertySource("classpath:email.Properties")
public class EmailConfig {

    Properties pt = new Properties();
    @Value("${mail.smtp.port}")
    private int port;

    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;

    @Value("${mail.smtp.auth}")
    private boolean auth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${mail.smtp.starttls.required}")
    private boolean starttls_required;

    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;

    @Bean
    public JavaMailSender javaMailService(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("JyHwa0702@gmail.com");
        javaMailSender.setPassword("urjbzsjocrnkicea");
        javaMailSender.setPort(port);

        pt.put("mail.smtp.socketFactory.port",socketPort);
        pt.put("mail.smtp.auth",socketPort);
        pt.put("mail.smtp.starttls.enable",starttls );
        pt.put("mail.smtp.starttls.required",starttls_required);
        pt.put("mail.smtp.socketFactory.fallback",fallback);
        pt.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(pt);
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

//    @Bean
//    public JavaMailSender javaMailSender(){
//        return new JavaMailSenderImpl();
//    }
}
