package com.myhome.services.unit;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.model.ForgotPasswordRequest;
import com.myhome.domain.Community;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.User;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.springdatajpa.MailSDJpaService;
import com.myhome.services.springdatajpa.SecurityTokenSDJpaService;
import com.myhome.services.springdatajpa.UserSDJpaService;
import helpers.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
  private final Duration TOKEN_LIFETIME = Duration.ofDays(1);

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
    SecurityToken emailConfirmToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, "token", resultUser);

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
    given(securityTokenService.createEmailConfirmToken(resultUser))
        .willReturn(emailConfirmToken);

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
    verify(securityTokenService).createEmailConfirmToken(resultUser);
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
    User user = new User(userDto.getName(), userDto.getUserId(), userDto.getEmail(), false,
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
  void confirmEmail() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    user.getUserTokens().add(testSecurityToken);
    given(securityTokenService.useToken(testSecurityToken))
        .willReturn(testSecurityToken);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));
    //    given(mailService.sendAccountConfirmed(user))
    //        .willReturn(true);

    // when
    boolean emailConfirmed =
        userService.confirmEmail(user.getUserId(), testSecurityToken.getToken());

    // then
    assertTrue(emailConfirmed);
    assertTrue(user.isEmailConfirmed());
    verify(securityTokenService).useToken(testSecurityToken);
    verify(userRepository).save(user);
    //    verify(mailService).sendAccountConfirmed(user);
  }

  @Test
  void confirmEmailWrongToken() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    user.getUserTokens().add(testSecurityToken);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed = userService.confirmEmail(user.getUserId(), "wrong-token");

    // then
    assertFalse(emailConfirmed);
    assertFalse(user.isEmailConfirmed());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  @Test
  void confirmEmailUsedToken() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    testSecurityToken.setUsed(true);
    user.getUserTokens().add(testSecurityToken);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed =
        userService.confirmEmail(user.getUserId(), testSecurityToken.getToken());

    // then
    assertFalse(emailConfirmed);
    assertFalse(user.isEmailConfirmed());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  @Test
  void confirmEmailNoToken() {
    // given
    User user = getDefaultUser();
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed = userService.confirmEmail(user.getUserId(), "any-token");

    // then
    assertFalse(emailConfirmed);
    assertFalse(user.isEmailConfirmed());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  @Test
  void confirmEmailAlreadyConfirmed() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    user.getUserTokens().add(testSecurityToken);
    user.setEmailConfirmed(true);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed =
        userService.confirmEmail(user.getUserId(), testSecurityToken.getToken());

    // then
    assertFalse(emailConfirmed);
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  @Test
  void findUserByEmailSuccess() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = getUserFromDto(userDto);

    given(userRepository.findByEmail(USER_EMAIL))
        .willReturn(user);
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> resultUserDtoOptional = userService.findUserByEmail(USER_EMAIL);

    // then
    assertTrue(resultUserDtoOptional.isPresent());
    UserDto createdUserDto = resultUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(0, createdUserDto.getCommunityIds().size());
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  @Test
  void findUserByEmailSuccessWithCommunityIds() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = getUserFromDto(userDto);

    Community firstCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);
    Community secCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);

    Set<Community> communities =
        Stream.of(firstCommunity, secCommunity).collect(Collectors.toSet());

    Set<String> communitiesIds = communities
        .stream()
        .map(Community::getCommunityId)
        .collect(Collectors.toSet());

    given(userRepository.findByEmail(USER_EMAIL))
        .willReturn(user);
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> resultUserDtoOptional = userService.findUserByEmail(USER_EMAIL);

    // then
    assertTrue(resultUserDtoOptional.isPresent());
    UserDto createdUserDto = resultUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(communitiesIds, createdUserDto.getCommunityIds());
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  @Test
  void findUserByEmailNotFound() {
    // given
    given(userRepository.findByEmail(USER_EMAIL))
        .willReturn(null);

    // when
    Optional<UserDto> resultUserDtoOptional = userService.findUserByEmail(USER_EMAIL);

    // then
    assertFalse(resultUserDtoOptional.isPresent());
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  @Test
  void requestResetPassword() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, null);
    given(securityTokenService.createPasswordResetToken(user))
        .willReturn(testSecurityToken);
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));
    given(mailService.sendPasswordRecoverCode(user, testSecurityToken.getToken()))
        .willReturn(true);

    // when
    boolean resetRequested = userService.requestResetPassword(forgotPasswordRequest);

    // then
    assertTrue(resetRequested);
    assertEquals(getUserSecurityToken(user, SecurityTokenType.RESET), testSecurityToken);
    verify(securityTokenService).createPasswordResetToken(user);
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verify(userRepository).save(user);
    verify(mailService).sendPasswordRecoverCode(user, testSecurityToken.getToken());
  }

  @Test
  void requestResetPasswordUserNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, user);
    given(securityTokenService.createPasswordResetToken(user))
        .willReturn(testSecurityToken);
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.empty());

    // when
    boolean resetRequested = userService.requestResetPassword(forgotPasswordRequest);

    // then
    assertFalse(resetRequested);
    assertNotEquals(getUserSecurityToken(user, SecurityTokenType.RESET), testSecurityToken);
    verifyNoInteractions(securityTokenService);
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPassword() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, user);
    user.getUserTokens().add(testSecurityToken);
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));
    given(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()))
        .willReturn(forgotPasswordRequest.getNewPassword());
    when(userRepository.save(user))
        .then(returnsFirstArg());
    given(mailService.sendPasswordSuccessfullyChanged(user))
        .willReturn(true);
    given(securityTokenService.useToken(testSecurityToken))
        .willReturn(testSecurityToken);

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertTrue(passwordChanged);
    assertEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verify(passwordEncoder).encode(forgotPasswordRequest.getNewPassword());
    verify(mailService).sendPasswordSuccessfullyChanged(user);
    verify(securityTokenService).useToken(testSecurityToken);
  }

  @Test
  void resetPasswordUserNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, user);
    user.getUserTokens().add(testSecurityToken);
    ;
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.empty());

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
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
    user.getUserTokens().add(testSecurityToken);
    ;
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    assertFalse(getUserSecurityToken(user, SecurityTokenType.RESET).isUsed());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPasswordTokenNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  @Test
  void resetPasswordTokenNotMatches() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, null);
    testSecurityToken.setToken("wrong-token");
    User user = getDefaultUser();
    user.getUserTokens().add(testSecurityToken);
    ;
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    assertNotNull(getUserSecurityToken(user, SecurityTokenType.RESET));
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
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
        false,
        request.getEncryptedPassword(),
        new HashSet<>(),
        new HashSet<>()
    );
  }

  private SecurityToken getUserSecurityToken(User user, SecurityTokenType tokenType) {
    return user.getUserTokens()
        .stream()
        .filter(token -> token.getTokenType() == tokenType)
        .findFirst()
        .orElse(null);
  }

  private User getDefaultUser() {
    return getUserFromDto(getDefaultUserDtoRequest());
  }

  private ForgotPasswordRequest getForgotPasswordRequest() {
    ForgotPasswordRequest request = new ForgotPasswordRequest();
    request.setEmail(USER_EMAIL);
    request.setNewPassword(NEW_USER_PASSWORD);
    request.setToken(PASSWORD_RESET_TOKEN);
    return request;
  }

  private SecurityToken getExpiredTestToken() {
    return new SecurityToken(SecurityTokenType.RESET, PASSWORD_RESET_TOKEN, LocalDate.now(),
        LocalDate.now().minusDays(TOKEN_LIFETIME.toDays()), false, null);
  }

  private SecurityToken getSecurityToken(SecurityTokenType tokenType, Duration lifetime,
      String token, User user) {
    LocalDate expireDate = LocalDate.now().plusDays(lifetime.toDays());
    return new SecurityToken(tokenType, token, LocalDate.now(), expireDate, false, user);
  }

  private SecurityToken getSecurityToken(SecurityTokenType tokenType, String token, User user) {
    LocalDate expireDate = LocalDate.now().plusDays(Duration.ofDays(1).toDays());
    return new SecurityToken(tokenType, token, LocalDate.now(), expireDate, false, user);
  }
}