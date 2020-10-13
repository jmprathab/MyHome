package com.myhome.services;

import com.myhome.domain.User;
import org.springframework.mail.MailSendException;

public interface MailService {

  void sendPasswordRecoverCode(User user, String randomCode) throws MailSendException;

  void sendPasswordSuccessfullyChanged(User user);
}
