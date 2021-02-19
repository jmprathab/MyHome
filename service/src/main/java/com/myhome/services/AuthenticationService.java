package com.myhome.services;

import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;

public interface AuthenticationService {
  AuthenticationData login(LoginRequest loginRequest);
}
