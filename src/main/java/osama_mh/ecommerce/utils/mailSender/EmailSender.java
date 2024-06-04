package osama_mh.ecommerce.utils.mailSender;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailSender {
    void sendEmail(String email, String subject, String content)throws MessagingException, UnsupportedEncodingException;
}
