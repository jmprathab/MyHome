package com.myhome.services;

import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.SecurityToken;

public interface SecurityTokenService {
  SecurityToken createSecurityToken(SecurityTokenType tokenType, int days);

  SecurityToken createPasswordResetToken();
}
