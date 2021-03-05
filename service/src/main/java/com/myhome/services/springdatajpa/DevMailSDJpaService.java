package com.myhome.services.springdatajpa;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "spring.mail.dev-mode", havingValue = "true", matchIfMissing = true)
public class DevMailSDJpaService implements MailService {

  @Override
  public boolean sendPasswordRecoverCode(User user, String randomCode) throws MailSendException {
    log.info(String.format("Password recover code sent to user with id= %s, code=%s", user.getUserId()), randomCode);
    return true;
  }

  @Override
  public boolean sendAccountConfirmed(User user) {
    log.info(String.format("Account confirmed message sent to user with id=%s", user.getUserId()));
    return true;
  }

  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    log.info(String.format("Password successfully changed message sent to user with id=%s", user.getUserId()));
    return true;
  }


  @Override
  public boolean sendAccountCreated(User user, SecurityToken emailConfirmToken) {
    log.info(String.format("Account created message sent to user with id=%s", user.getUserId()));
    return true;
  }


}
