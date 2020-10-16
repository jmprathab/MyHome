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
    SecurityToken newSecurityToken = new SecurityToken(tokenType, token, creationDate, expiryDate, false);
    newSecurityToken = securityTokenRepository.save(newSecurityToken);
    return newSecurityToken;
  }

  @Override
  public SecurityToken createPasswordResetToken() {
    return createSecurityToken(SecurityTokenType.RESET, passResetTokenTime);
  }

  private Date getDateAfterDays(Date date, int liveTimeSeconds) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.SECOND, liveTimeSeconds);
    return cal.getTime();
  }

  private LocalDate getDateAfterDays(LocalDate date, Duration liveTimeSeconds) {
    date.plus(liveTimeSeconds);
    return date;
  }
}
