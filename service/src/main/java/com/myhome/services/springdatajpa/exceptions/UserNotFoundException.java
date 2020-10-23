package com.myhome.services.springdatajpa.exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String userId) {
    super(String.format("User with %s not found:404", userId));
  }
}
