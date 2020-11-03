package com.myhome.services;

import com.myhome.domain.User;
import org.springframework.mail.MailSendException;

public interface MailService {

  boolean sendPasswordRecoverCode(User user, String randomCode) throws MailSendException;

  boolean sendPasswordSuccessfullyChanged(User user);
}
