package com.myhome.services.unit;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.SecurityTokenType;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.services.springdatajpa.SecurityTokenSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecurityTokenSDJpaServiceTest {

  private final Duration TEST_TOKEN_LIFETIME_SECONDS = Duration.ofDays(1);

  @Mock
  private SecurityTokenRepository securityTokenRepository;

  @InjectMocks
  private SecurityTokenSDJpaService securityTokenSDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(securityTokenSDJpaService, "passResetTokenTime",
        TEST_TOKEN_LIFETIME_SECONDS);
  }

  @Test
  void createSecurityToken() {
    // given
    SecurityTokenType testTokenType = SecurityTokenType.RESET;
    when(securityTokenRepository.save(any()))
        .then(returnsFirstArg());

    // when
    SecurityToken actualSecurityToken = securityTokenSDJpaService.createSecurityToken(testTokenType, TEST_TOKEN_LIFETIME_SECONDS);
    LocalDate creationDate = actualSecurityToken.getCreationDate();
    LocalDate expiryDate = actualSecurityToken.getExpiryDate();
    Duration lifetime = Duration.between(creationDate.atStartOfDay(), expiryDate.atStartOfDay());

    // then
    assertEquals(actualSecurityToken.getTokenType(), testTokenType);
    assertTrue(creationDate.isBefore(expiryDate));
    assertEquals(lifetime, TEST_TOKEN_LIFETIME_SECONDS);
    assertNotNull(actualSecurityToken.getToken());
    verify(securityTokenRepository).save(any());
  }

  @Test
  void createPasswordResetToken() {
    // given
    when(securityTokenRepository.save(any()))
        .then(returnsFirstArg());

    // when
    SecurityToken actualSecurityToken = securityTokenSDJpaService.createPasswordResetToken();
    LocalDate creationDate = actualSecurityToken.getCreationDate();
    LocalDate expiryDate = actualSecurityToken.getExpiryDate();
    Duration lifetime = Duration.between(creationDate.atStartOfDay(), expiryDate.atStartOfDay());

    // then
    assertEquals(actualSecurityToken.getTokenType(), SecurityTokenType.RESET);
    assertTrue(creationDate.isBefore(expiryDate));
    assertEquals(lifetime, TEST_TOKEN_LIFETIME_SECONDS);
    assertNotNull(actualSecurityToken.getToken());
    verify(securityTokenRepository).save(any());
  }

}
