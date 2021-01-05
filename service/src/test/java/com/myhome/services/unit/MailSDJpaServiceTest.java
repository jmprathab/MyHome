package com.myhome.services.unit;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.springdatajpa.MailSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

class MailSDJpaServiceTest {

  @Mock
  private JavaMailSender mailSender;
  @Mock
  private ITemplateEngine emailTemplateEngine;
  @Mock
  private ResourceBundleMessageSource messageSource;

  @InjectMocks
  private MailSDJpaService mailSDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(mailSDJpaService, "username", "username");
    ReflectionTestUtils.setField(mailSDJpaService, "passwordChangedMailTemplateName", "");
    ReflectionTestUtils.setField(mailSDJpaService, "passwordRecoverMailTemplateName", "");
    ReflectionTestUtils.setField(mailSDJpaService, "accountCreatedMailTemplateName", "");
    ReflectionTestUtils.setField(mailSDJpaService, "accountConfirmedMailTemplateName", "");
    ReflectionTestUtils.setField(mailSDJpaService, "port", "");
    ReflectionTestUtils.setField(mailSDJpaService, "host", "");

  }

  @Test
  void sendPasswordRecoverCodeMailException() {
    // given
    MimeMessage mimeMessage = new MimeMessage((Session)null);
    User user = getTestUser();
    given(emailTemplateEngine.process(eq(""), any(Context.class)))
        .willReturn("HTML");
    given(mailSender.createMimeMessage())
        .willReturn(mimeMessage);
    doThrow(MailSendException.class).when(mailSender).send(mimeMessage);

    // when
    boolean mailSent = mailSDJpaService.sendPasswordRecoverCode(user, "test-token");

    // then
    assertFalse(mailSent);
  }

  @Test
  void sendPasswordSuccessfullyChangedMailException() {
    // given
    MimeMessage mimeMessage = new MimeMessage((Session)null);
    User user = getTestUser();
    given(emailTemplateEngine.process(eq(""), any(Context.class)))
        .willReturn("HTML");
    given(mailSender.createMimeMessage())
        .willReturn(mimeMessage);
    doThrow(MailSendException.class).when(mailSender).send(mimeMessage);

    // when
    boolean mailSent = mailSDJpaService.sendPasswordSuccessfullyChanged(user);

    // then
    assertFalse(mailSent);
  }

  @Test
  void sendEmailConfirmedMailException() {
    // given
    MimeMessage mimeMessage = new MimeMessage((Session)null);
    User user = getTestUser();
    given(emailTemplateEngine.process(eq(""), any(Context.class)))
        .willReturn("HTML");
    given(mailSender.createMimeMessage())
        .willReturn(mimeMessage);
    doThrow(MailSendException.class).when(mailSender).send(mimeMessage);

    // when
    boolean mailSent = mailSDJpaService.sendAccountConfirmed(user);

    // then
    assertFalse(mailSent);
  }

  @Test
  void sendEmailCreatedMailException() {
    // given
    SecurityToken token = new SecurityToken();
    token.setToken("token");
    MimeMessage mimeMessage = new MimeMessage((Session)null);
    User user = getTestUser();
    given(emailTemplateEngine.process(eq(""), any(Context.class)))
        .willReturn("HTML");
    given(mailSender.createMimeMessage())
        .willReturn(mimeMessage);
    doThrow(MailSendException.class).when(mailSender).send(mimeMessage);

    // when
    boolean mailSent = mailSDJpaService.sendAccountCreated(user, token);

    // then
    assertFalse(mailSent);
  }

  private User getTestUser() {
    User user = new User();
    user.setEmail("test-email");
    return user;
  }

}