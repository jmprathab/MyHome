package com.myhome.services.springdatajpa;

import com.myhome.configuration.properties.mail.MailProperties;
import com.myhome.configuration.properties.mail.MailTemplatesNames;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(value = "spring.mail.devMode", havingValue = "false", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class MailSDJpaService implements MailService {

  private final ITemplateEngine emailTemplateEngine;
  private final JavaMailSender mailSender;
  private final ResourceBundleMessageSource messageSource;
  private final MailProperties mailProperties;

  @Override
  public boolean sendPasswordRecoverCode(User user, String randomCode) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    templateModel.put("recoverCode", randomCode);
    String passwordRecoverSubject = getLocalizedMessage("locale.EmailSubject.passwordRecover");
    boolean mailSent = send(user.getEmail(), passwordRecoverSubject,
        MailTemplatesNames.PASSWORD_RESET.filename, templateModel);
    return mailSent;
  }

  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    String passwordChangedSubject = getLocalizedMessage("locale.EmailSubject.passwordChanged");
    boolean mailSent = send(user.getEmail(), passwordChangedSubject,
        MailTemplatesNames.PASSWORD_CHANGED.filename, templateModel);
    return mailSent;
  }

  @Override
  public boolean sendAccountCreated(User user, SecurityToken emailConfirmToken) {
    Map<String, Object> templateModel = new HashMap<>();
    String emailConfirmLink = getAccountConfirmLink(user, emailConfirmToken);
    templateModel.put("username", user.getName());
    templateModel.put("emailConfirmLink", emailConfirmLink);
    String accountCreatedSubject = getLocalizedMessage("locale.EmailSubject.accountCreated");
    boolean mailSent = send(user.getEmail(), accountCreatedSubject,
        MailTemplatesNames.ACCOUNT_CREATED.filename, templateModel);
    return mailSent;
  }

  @Override
  public boolean sendAccountConfirmed(User user) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    String accountConfirmedSubject = getLocalizedMessage("locale.EmailSubject.accountConfirmed");
    boolean mailSent = send(user.getEmail(), accountConfirmedSubject,
        MailTemplatesNames.ACCOUNT_CONFIRMED.filename, templateModel);
    return mailSent;
  }

  private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(mailProperties.getUsername());
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

  private String getAccountConfirmLink(User user, SecurityToken token) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .replacePath(null)
        .build()
        .toUriString();
    return String.format("%s/users/%s/email-confirm/%s", baseUrl, user.getUserId(), token.getToken());
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
