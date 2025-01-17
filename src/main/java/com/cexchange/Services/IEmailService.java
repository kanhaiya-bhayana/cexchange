package com.cexchange.Services;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;

public interface IEmailService {
    boolean SendEmail(String email, String body) throws MessagingException;
}
