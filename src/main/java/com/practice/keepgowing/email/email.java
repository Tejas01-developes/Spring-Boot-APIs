package com.practice.keepgowing.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class email {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    public email(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendmail(String to,String subject,String templatename) throws MessagingException {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);


        Context context=new Context();

        String htmlcontent=templateEngine.process(templatename,context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlcontent,true);
        helper.setFrom("${Spring.mail.username}");
        mailSender.send(message);



    }




}
