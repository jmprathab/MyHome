package com.myhome.services.unit;

import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.SecurityToken;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.services.springdatajpa.SecurityTokenSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecurityTokenSDJpaServiceTest {

  private final int TEST_TOKEN_LIFETIME_SECONDS = 60 * 60 * 24;
  private final int TEST_TOKEN_LIFETIME_SECONDS_UNIX = TEST_TOKEN_LIFETIME_SECONDS * 1000;

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
    Date creationDate = actualSecurityToken.getCreationDate();
    Date expiryDate = actualSecurityToken.getExpiryDate();
    long lifetime = expiryDate.getTime() - creationDate.getTime();

    // then
    assertEquals(actualSecurityToken.getTokenType(), testTokenType);
    assertTrue(creationDate.before(expiryDate));
    assertEquals(lifetime, TEST_TOKEN_LIFETIME_SECONDS_UNIX);
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
    Date creationDate = actualSecurityToken.getCreationDate();
    Date expiryDate = actualSecurityToken.getExpiryDate();
    long lifetime = expiryDate.getTime() - creationDate.getTime();

    // then
    assertEquals(actualSecurityToken.getTokenType(), SecurityTokenType.RESET);
    assertTrue(creationDate.before(expiryDate));
    assertEquals(lifetime, TEST_TOKEN_LIFETIME_SECONDS_UNIX);
    assertNotNull(actualSecurityToken.getToken());
    verify(securityTokenRepository).save(any());
  }

}
