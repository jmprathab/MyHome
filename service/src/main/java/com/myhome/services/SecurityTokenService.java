package com.myhome.services;

import com.myhome.domain.MyHomeTokenType;
import com.myhome.domain.SecurityToken;

public interface SecurityTokenService {
  SecurityToken createSecurityToken(MyHomeTokenType tokenType, int days);

  SecurityToken createPasswordResetToken();
}
