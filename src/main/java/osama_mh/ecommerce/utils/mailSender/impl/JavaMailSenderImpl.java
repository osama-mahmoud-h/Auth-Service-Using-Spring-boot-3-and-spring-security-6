package osama_mh.ecommerce.utils.mailSender.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import osama_mh.ecommerce.utils.mailSender.EmailSender;

import java.io.UnsupportedEncodingException;

@Service
public class JavaMailSenderImpl implements EmailSender {

//    @Value("${spring.mail.username}")
//    private String senderEmail;

    private final JavaMailSender mailSender;
    @Autowired
    public JavaMailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }
    @Override
    public void sendEmail(String email, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

       // System.out.println("from: " + senderEmail+" to: " + email + " subject: " + subject + " content: " + content);
       // helper.setFrom(senderEmail, "Azzam Market");
        helper.setTo(email);

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
