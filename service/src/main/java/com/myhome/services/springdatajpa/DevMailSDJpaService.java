package com.myhome.services.springdatajpa;

import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "spring.mail.debug", havingValue = "true", matchIfMissing = true)
public class DevMailSDJpaService implements MailService {

  @Override
  public void sendPasswordRecoverCode(User user, String randomCode) throws MailSendException {
    log.trace(String.format("Password recover code sent to user with id= %s, code=%s", user.getUserId()), randomCode);
  }

  @Override
  public void sendPasswordSuccessfullyChanged(User user) {
    log.trace(String.format("Password successfully changed message sent to user with id=%s", user.getUserId()));
  }
}
