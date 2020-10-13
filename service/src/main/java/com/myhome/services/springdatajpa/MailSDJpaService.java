package com.myhome.services.springdatajpa;

import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSDJpaService implements MailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String username;

  private void send(String emailTo, String subject, String message) throws MailSendException {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(username);
    mailMessage.setTo(emailTo);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);
    mailSender.send(mailMessage);
  }

  @Override
  public void sendPasswordRecoverCode(User user, String randomCode) throws MailSendException {
    String message = String.format(
        "Hello, %s! \n" + "this is your password recover code %s",
        user.getName(), randomCode);
    send(user.getEmail(), "'My home' Password recover code", message);
  }

  @Override
  public void sendPasswordSuccessfullyChanged(User user) {
    String message = String.format(
        "Hello, %s! \n" + "your password was successfully changed!",
        user.getName());
    send(user.getEmail(), "'My home' Password reset successfully", message);
  }

}
