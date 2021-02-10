package com.myhome.services;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;

public interface SecurityTokenService {

  SecurityToken createEmailConfirmToken(User owner);

  SecurityToken createPasswordResetToken(User owner);

  SecurityToken useToken(SecurityToken token);
}
