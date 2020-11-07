package com.myhome.services.unit;

import com.myhome.domain.User;
import com.myhome.services.springdatajpa.MailSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class MailSDJpaServiceTest {

  @Mock
  private JavaMailSender mailSender;

  @InjectMocks
  private MailSDJpaService mailSDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void sendPasswordRecoverCodeMailException() {
    // given
    User user = getTestUser();
    doThrow(mock(MailException.class)).when(mailSender).send((SimpleMailMessage) any());

    // when
    boolean mailSent = mailSDJpaService.sendPasswordRecoverCode(user, "test-token");

    // then
    assertFalse(mailSent);
  }

  @Test
  void sendPasswordSuccessfullyChangedMailException() {
    // given
    User user = getTestUser();
    doThrow(mock(MailException.class)).when(mailSender).send((SimpleMailMessage) any());

    // when
    boolean mailSent = mailSDJpaService.sendPasswordSuccessfullyChanged(user);

    // then
    assertFalse(mailSent);
  }

  private User getTestUser() {
    User user = new User();
    user.setEmail("test-email");
    return user;
  }

}