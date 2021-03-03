package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.exceptions.CredentialsIncorrectException;
import com.myhome.controllers.exceptions.UserNotFoundException;
import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;
import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import com.myhome.services.AuthenticationService;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationSDJpaService implements AuthenticationService {

  @Value("${token.expiration_time}")
  private final Duration tokenExpirationTime;
  @Value("${token.secret}")
  private final String tokenSecret;

  private final UserSDJpaService userSDJpaService;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthenticationData login(LoginRequest loginRequest) {
    log.trace("Received login request");
    final UserDto userDto = userSDJpaService.findUserByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new UserNotFoundException(loginRequest.getEmail()));
    if (!isPasswordMatching(loginRequest.getPassword(), userDto.getEncryptedPassword())) {
      throw new CredentialsIncorrectException(userDto.getUserId());
    }
    final AppJwt jwtToken = createJwt(userDto);
    final String encodedToken = appJwtEncoderDecoder.encode(jwtToken, tokenSecret);
    return new AuthenticationData(encodedToken, userDto.getUserId());
  }

  private boolean isPasswordMatching(String requestPassword, String databasePassword) {
    return passwordEncoder.matches(requestPassword, databasePassword);
  }

  private AppJwt createJwt(UserDto userDto) {
    final LocalDateTime expirationTime = LocalDateTime.now().plus(tokenExpirationTime);
    return AppJwt.builder()
        .userId(userDto.getUserId())
        .expiration(expirationTime)
        .build();
  }
}
