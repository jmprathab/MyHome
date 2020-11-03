package com.myhome.services;

import com.myhome.domain.User;

public interface MailService {

  boolean sendPasswordRecoverCode(User user, String randomCode);

  boolean sendPasswordSuccessfullyChanged(User user);
}
