package com.myhome.configuration.properties.mail;

public enum MailTemplatesNames {
  PASSWORD_RESET("passwordRecoverCode"),
  PASSWORD_CHANGED("passwordChanged"),
  ACCOUNT_CREATED("accountCreated"),
  ACCOUNT_CONFIRMED("accountConfirmed");

  MailTemplatesNames(String templateFileName) {}
}
