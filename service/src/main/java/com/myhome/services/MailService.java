package com.myhome.services;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;

public interface MailService {

  boolean sendPasswordRecoverCode(User user, String randomCode);

  boolean sendAccountCreated(User user, SecurityToken emailConfirmToken);

  boolean sendPasswordSuccessfullyChanged(User user);

  boolean sendAccountConfirmed(User user);
}
