package com.myhome.configuration.properties.mail;

public enum MailTemplatesNames {
  PASSWORD_RESET("passwordRecoverCode"),
  PASSWORD_CHANGED("passwordChanged"),
  ACCOUNT_CREATED("accountCreated"),
  ACCOUNT_CONFIRMED("accountConfirmed");

  public final String filename;
  MailTemplatesNames(String fileName) {
    this.filename = fileName;
  }
}
