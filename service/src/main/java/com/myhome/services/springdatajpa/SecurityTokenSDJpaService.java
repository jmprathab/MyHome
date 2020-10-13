package com.myhome.services.springdatajpa;

import com.myhome.domain.MyHomeTokenType;
import com.myhome.domain.SecurityToken;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.services.SecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityTokenSDJpaService implements SecurityTokenService {

  private final SecurityTokenRepository securityTokenRepository;

  @Value("${users.passResetTokenTime}")
  private int passResetTokenTime;

  @Override
  public SecurityToken createSecurityToken(MyHomeTokenType tokenType, int liveTimeSeconds) {
    String token = UUID.randomUUID().toString();
    Date creationDate = new Date();
    Date expiryDate = getDateAfterDays(creationDate, liveTimeSeconds);
    SecurityToken newSecurityToken = new SecurityToken(tokenType, token, creationDate, expiryDate);
    newSecurityToken = securityTokenRepository.save(newSecurityToken);
    return newSecurityToken;
  }

  @Override
  public SecurityToken createPasswordResetToken() {
    return createSecurityToken(MyHomeTokenType.RESET, passResetTokenTime);
  }

  private Date getDateAfterDays(Date date, int liveTimeSeconds) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.SECOND, liveTimeSeconds);
    return cal.getTime();
  }
}
