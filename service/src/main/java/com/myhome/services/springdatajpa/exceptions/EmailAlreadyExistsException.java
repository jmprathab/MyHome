package com.myhome.services.springdatajpa.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException() {
      super("Email already exists:419");
  }
}
