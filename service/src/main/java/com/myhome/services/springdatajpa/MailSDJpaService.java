package com.myhome.services.springdatajpa;

import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "spring.mail.debug", havingValue = "false", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class MailSDJpaService implements MailService {

  private final JavaMailSender mailSender;

  private final String PASSWORD_RECOVER_EMAIL_SUBJECT = "'My home' Password recover code";
  private final String PASSWORD_RECOVER_MAIL_TEXT = "Hello, %s! \\n\" + \"this is your password recover code %s";
  private final String PASSWORD_CHANGED_MAIL_SUBJECT = "'My home' Password reset successfully";
  private final String PASSWORD_CHANGED_MAIL_TEXT = "Hello, %s! \\n\" + \"your password was successfully changed!";


  @Value("${spring.mail.username}")
  private String username;

  private boolean send(String emailTo, String subject, String message) {
    try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(username);
      mailMessage.setTo(emailTo);
      mailMessage.setSubject(subject);
      mailMessage.setText(message);
      mailSender.send(mailMessage);
    } catch (MailException mailException) {
      log.error("Mail send error!", mailException);
      return false;
    }
    return true;
  }

  @Override
  public boolean sendPasswordRecoverCode(User user, String randomCode) {
    String message = String.format(PASSWORD_RECOVER_MAIL_TEXT, user.getName(), randomCode);
    boolean mailSent = send(user.getEmail(), PASSWORD_RECOVER_EMAIL_SUBJECT, message);
    return mailSent;
  }

  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    String message = String.format(PASSWORD_CHANGED_MAIL_TEXT, user.getName());
    boolean mailSent = send(user.getEmail(), PASSWORD_CHANGED_MAIL_SUBJECT, message);
    return mailSent;
  }

}
