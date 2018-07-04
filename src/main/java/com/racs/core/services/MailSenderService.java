package com.racs.core.services;

import org.springframework.mail.javamail.JavaMailSender;

import com.racs.core.entities.User;

import javax.mail.MessagingException;

public interface MailSenderService {
    JavaMailSender getJavaMailSender();
    void sendEmailToken(String destinatario, String urlToken, JavaMailSender emailSender) throws MessagingException;
    void sendEmailNewUser(User userSso, String passworTemp, JavaMailSender emailSender) throws MessagingException;
}
