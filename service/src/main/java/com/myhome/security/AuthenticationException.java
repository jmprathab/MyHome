package com.myhome.security;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
      super("Failed to login. User not found:403");
    }
}
