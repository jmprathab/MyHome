/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.services.unit;

import helpers.TestUtils;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.controllers.request.ForgotPasswordRequest;
import com.myhome.domain.Community;
import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.springdatajpa.MailSDJpaService;
import com.myhome.services.springdatajpa.SecurityTokenSDJpaService;
import com.myhome.services.springdatajpa.UserSDJpaService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UserSDJpaServiceTest {

  private final String USER_ID = "test-user-id";
  private final String USERNAME = "test-user-name";
  private final String USER_EMAIL = "test-user-email";
  private final String USER_PASSWORD = "test-user-password";
  private final String NEW_USER_PASSWORD = "test-user-new-password";
  private final String PASSWORD_RESET_TOKEN = "test-token";
  private final int TOKEN_LIFETIME = 10000;
  private final int EXPIRED_TOKEN_LIFETIME = -10000;

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private SecurityTokenSDJpaService securityTokenService;
  @Mock
  private MailSDJpaService mailService;
  @Mock
  private SecurityTokenRepository securityTokenRepository;
  @InjectMocks
  private UserSDJpaService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createUserSuccess() {
    // given
    UserDto request = getDefaultUserDtoRequest();
    User resultUser = getUserFromDto(request);
    UserDto response = UserDto.builder()
        .id(resultUser.getId())
        .userId(resultUser.getUserId())
        .name(resultUser.getName())
        .encryptedPassword(resultUser.getEncryptedPassword())
        .communityIds(new HashSet<>())
        .build();

    given(userRepository.findByEmail(request.getEmail()))
        .willReturn(null);
    given(passwordEncoder.encode(request.getPassword()))
        .willReturn(request.getPassword());
    given(userMapper.userDtoToUser(request))
        .willReturn(resultUser);
    given(userRepository.save(resultUser))
        .willReturn(resultUser);
    given(userMapper.userToUserDto(resultUser))
        .willReturn(response);

    // when
    Optional<UserDto> createdUserDtoOptional = userService.createUser(request);

    // then
    assertTrue(createdUserDtoOptional.isPresent());
    UserDto createdUserDto = createdUserDtoOptional.get();
    assertEquals(response, createdUserDto);
    assertEquals(0, createdUserDto.getCommunityIds().size());
    verify(userRepository).findByEmail(request.getEmail());
    verify(passwordEncoder).encode(request.getPassword());
    verify(userRepository).save(resultUser);
  }

  @Test
  void createUserEmailExists() {
    // given
    UserDto request = getDefaultUserDtoRequest();
    User user = getUserFromDto(request);

    given(userRepository.findByEmail(request.getEmail()))
        .willReturn(user);

    // when
    Optional<UserDto> createdUserDto = userService.createUser(request);

    // then
    assertFalse(createdUserDto.isPresent());
    verify(userRepository).findByEmail(request.getEmail());
  }

  @Test
  void getUserDetailsSuccess() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = getUserFromDto(userDto);

    given(userRepository.findByUserIdWithCommunities(USER_ID))
        .willReturn(Optional.of(user));
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> createdUserDtoOptional = userService.getUserDetails(USER_ID);

    // then
    assertTrue(createdUserDtoOptional.isPresent());
    UserDto createdUserDto = createdUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(0, createdUserDto.getCommunityIds().size());
    verify(userRepository).findByUserIdWithCommunities(USER_ID);
  }

  @Test
  void getUserDetailsSuccessWithCommunityIds() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = new User(userDto.getName(), userDto.getUserId(), userDto.getEmail(),
        userDto.getEncryptedPassword(), new HashSet<>(), null);

    Community firstCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);
    Community secCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);

    Set<Community> communities =
        Stream.of(firstCommunity, secCommunity).collect(Collectors.toSet());

    Set<String> communitiesIds = communities
        .stream()
        .map(community -> community.getCommunityId())
        .collect(Collectors.toSet());

    given(userRepository.findByUserIdWithCommunities(USER_ID))
        .willReturn(Optional.of(user));
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> createdUserDtoOptional = userService.getUserDetails(USER_ID);

    // then
    assertTrue(createdUserDtoOptional.isPresent());
    UserDto createdUserDto = createdUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(communitiesIds, createdUserDto.getCommunityIds());
    verify(userRepository).findByUserIdWithCommunities(USER_ID);
  }

  @Test
  void getUserDetailsNotFound() {
    // given
    given(userRepository.findByUserIdWithCommunities(USER_ID))
        .willReturn(Optional.empty());

    // when
    Optional<UserDto> createdUserDto = userService.getUserDetails(USER_ID);

    // then
    assertFalse(createdUserDto.isPresent());
    verify(userRepository).findByUserIdWithCommunities(USER_ID);
  }

  @Test
  void requestResetPassword() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken = getTestSecurityToken();
    given(securityTokenService.createPasswordResetToken())
        .willReturn(testSecurityToken);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.of(user));

    // when
    boolean passwordResetRequested = userService.requestResetPassword(forgotPasswordRequest);

    // then
    assertTrue(passwordResetRequested);
    assertEquals(user.getPasswordResetToken(), testSecurityToken);
    verify(securityTokenService).createPasswordResetToken();
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verify(userRepository).save(user);
    verify(mailService).sendPasswordRecoverCode(user, testSecurityToken.getToken());
  }

  @Test
  void requestResetPasswordUserNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken = getTestSecurityToken();
    given(securityTokenService.createPasswordResetToken())
        .willReturn(testSecurityToken);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.empty());

    // when
    boolean passwordResetRequested = userService.requestResetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordResetRequested);
    assertNotEquals(user.getPasswordResetToken(), testSecurityToken);
    verify(securityTokenService).createPasswordResetToken();
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verify(userRepository, never()).save(user);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPassword() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken = getTestSecurityToken();
    User user = getDefaultUser();
    user.setPasswordResetToken(testSecurityToken);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.of(user));
    given(passwordEncoder.encode(forgotPasswordRequest.newPassword))
        .willReturn(forgotPasswordRequest.newPassword);
    when(userRepository.save(user))
        .then(returnsFirstArg());

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertTrue(passwordChanged);
    assertEquals(user.getEncryptedPassword(), forgotPasswordRequest.newPassword);
    assertNull(user.getPasswordResetToken());
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verify(securityTokenRepository).delete(testSecurityToken);
    verify(passwordEncoder).encode(forgotPasswordRequest.newPassword);
    verify(mailService).sendPasswordSuccessfullyChanged(user);
  }

  @Test
  void resetPasswordUserNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken = getTestSecurityToken();
    User user = getDefaultUser();
    user.setPasswordResetToken(testSecurityToken);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.empty());

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.newPassword);
    assertNotNull(user.getPasswordResetToken());
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPasswordTokenExpired() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken = getExpiredTestToken();
    User user = getDefaultUser();
    user.setPasswordResetToken(testSecurityToken);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.newPassword);
    assertNotNull(user.getPasswordResetToken());
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPasswordTokenNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    user.setPasswordResetToken(null);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.newPassword);
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPasswordTokenNotMatches() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken = getTestSecurityToken();
    testSecurityToken.setToken("wrong-token");
    User user = getDefaultUser();
    user.setPasswordResetToken(testSecurityToken);
    given(userRepository.findByEmailWithPasswordResetToken(forgotPasswordRequest.email))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.newPassword);
    assertNotNull(user.getPasswordResetToken());
    verify(userRepository).findByEmailWithPasswordResetToken(forgotPasswordRequest.email);
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  private UserDto getDefaultUserDtoRequest() {
    return UserDto.builder()
        .userId(USER_ID)
        .name(USERNAME)
        .email(USER_EMAIL)
        .encryptedPassword(USER_PASSWORD)
        .communityIds(new HashSet<>())
        .build();
  }

  private User getUserFromDto(UserDto request) {
    return new User(
        request.getName(),
        request.getUserId(),
        request.getEmail(),
        request.getEncryptedPassword(),
        new HashSet<>(),
        null
    );
  }

  private User getDefaultUser() {
    return getUserFromDto(getDefaultUserDtoRequest());
  }

  private ForgotPasswordRequest getForgotPasswordRequest() {
    return new ForgotPasswordRequest(USER_EMAIL, PASSWORD_RESET_TOKEN, NEW_USER_PASSWORD);
  }

  private SecurityToken getTestSecurityToken(int lifetime) {
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.SECOND, lifetime);
    return new SecurityToken(SecurityTokenType.RESET, PASSWORD_RESET_TOKEN, new Date(), cal.getTime());
  }

  private SecurityToken getTestSecurityToken() {
    return getTestSecurityToken(TOKEN_LIFETIME);
  }

  private SecurityToken getExpiredTestToken() {
    return getTestSecurityToken(EXPIRED_TOKEN_LIFETIME);
  }

}