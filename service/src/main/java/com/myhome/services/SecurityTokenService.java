package com.myhome.services;

import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.SecurityToken;

import java.time.Duration;

public interface SecurityTokenService {

  SecurityToken createSecurityToken(SecurityTokenType tokenType, Duration liveTimeSeconds);

  SecurityToken createPasswordResetToken();
}
