package com.cexchange.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    private  JavaMailSender _mailSender;

    @Value("${emailSettings.subject}")
    private String Subject;


    @Override
    public boolean SendEmail(String toEmail, String body) throws MessagingException {
        MimeMessage mimeMessage = _mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,"utf-8");

        String messageBody = "Your verification code is: " + body;
        mimeMessageHelper.setSubject(Subject);
        mimeMessageHelper.setText(messageBody);
        mimeMessageHelper.setTo(toEmail);

        try{
            _mailSender.send(mimeMessage);
            return true;
        }
        catch (Exception ex){
            throw new MailSendException(ex.getMessage());
        }
    }
}
