package com.myhome.services.springdatajpa;

import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.SecurityToken;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.services.SecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityTokenSDJpaService implements SecurityTokenService {

  private final SecurityTokenRepository securityTokenRepository;

  @Value("${tokens.reset.expiration}")
  private Duration passResetTokenTime;

  @Override
  public SecurityToken createSecurityToken(SecurityTokenType tokenType, Duration liveTimeSeconds) {
    String token = UUID.randomUUID().toString();
    LocalDate creationDate = LocalDate.now();
    LocalDate expiryDate = getDateAfterDays(LocalDate.now(), liveTimeSeconds);
    SecurityToken newSecurityToken = new SecurityToken(tokenType, token, creationDate, expiryDate, false, null);
    newSecurityToken = securityTokenRepository.save(newSecurityToken);
    return newSecurityToken;
  }

  @Override
  public SecurityToken createPasswordResetToken() {
    return createSecurityToken(SecurityTokenType.RESET, passResetTokenTime);
  }

  @Override
  public void useToken(SecurityToken token) {
    token.setUsed(true);
    securityTokenRepository.save(token);
  }

  private LocalDate getDateAfterDays(LocalDate date, Duration liveTime) { return date.plus(liveTime); }
}
