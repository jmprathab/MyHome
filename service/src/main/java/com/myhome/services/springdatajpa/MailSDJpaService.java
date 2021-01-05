package com.myhome.services.springdatajpa;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(value = "spring.mail.dev-mode", havingValue = "false", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class MailSDJpaService implements MailService {

  private final ITemplateEngine emailTemplateEngine;
  private final JavaMailSender mailSender;
  private final ResourceBundleMessageSource messageSource;

  @Value("${spring.mail.username}")
  private String username;
  @Value("${spring.mail.templates.names.password-changed}")
  private String passwordChangedMailTemplateName;
  @Value("${spring.mail.templates.names.password-reset}")
  private String passwordRecoverMailTemplateName;
  @Value("${spring.mail.templates.names.account-created}")
  private String accountCreatedMailTemplateName;
  @Value("${spring.mail.templates.names.account-confirmed}")
  private String accountConfirmedMailTemplateName;

  @Value("${server.host}")
  private String host;
  @Value("${server.port}")
  private String port;


  private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(username);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlBody, true);
      mailSender.send(message);
  }

  private boolean send(String emailTo, String subject, String templateName, Map<String, Object> templateModel) {
    try {
      Context thymeleafContext = new Context(LocaleContextHolder.getLocale());
      thymeleafContext.setVariables(templateModel);
      String htmlBody = emailTemplateEngine.process(templateName, thymeleafContext);
      sendHtmlMessage(emailTo, subject, htmlBody);
    } catch (MailException | MessagingException mailException) {
      log.error("Mail send error!", mailException);
      return false;
    }
    return true;
  }

  @Override
  public boolean sendPasswordRecoverCode(User user, String randomCode) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    templateModel.put("recoverCode", randomCode);
    String passwordRecoverSubject = getLocalizedMessage("locale.EmailSubject.passwordRecover");
    boolean mailSent = send(user.getEmail(), passwordRecoverSubject, passwordRecoverMailTemplateName,  templateModel);
    return mailSent;
  }

  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    String passwordChangedSubject = getLocalizedMessage("locale.EmailSubject.passwordChanged");
    boolean mailSent = send(user.getEmail(), passwordChangedSubject, passwordChangedMailTemplateName, templateModel);
    return mailSent;
  }

  @Override
  public boolean sendAccountCreated(User user, SecurityToken emailConfirmToken) {
    Map<String, Object> templateModel = new HashMap<>();
    String emailConfirmLink = getAccountConfirmLink(user, emailConfirmToken);
    templateModel.put("username", user.getName());
    templateModel.put("emailConfirmLink", emailConfirmLink);
    String accountCreatedSubject = getLocalizedMessage("locale.EmailSubject.accountCreated");
    boolean mailSent = send(user.getEmail(), accountCreatedSubject, accountCreatedMailTemplateName, templateModel);
    return mailSent;
  }

  @Override
  public boolean sendAccountConfirmed(User user) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    String accountConfirmedSubject = getLocalizedMessage("locale.EmailSubject.accountConfirmed");
    boolean mailSent = send(user.getEmail(), accountConfirmedSubject, accountConfirmedMailTemplateName, templateModel);
    return mailSent;
  }

  private String getAccountConfirmLink(User user, SecurityToken token) {
    return String.format("%s:%s/users/%s/email-confirm/%s", host, port, user.getUserId(), token.getToken());
  }

  private String getLocalizedMessage(String prop) {
    String message = "";
    try {
      message = messageSource.getMessage(prop, null, LocaleContextHolder.getLocale());
    } catch (Exception e) {
      message = prop + ": localization error";
    }
    return message;
  }

}
