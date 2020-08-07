package com.myhome.controllers.unit;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.User;
import com.myhome.repositories.UserRepository;
import com.myhome.services.CommunityService;
import com.myhome.services.springdatajpa.UserSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserSDJpaServiceTest {

  private String USER_ID = "test-user-id";
  private String USER_NAME = "test-user-id";
  private String USER_EMAIL = "test-user-id";
  private String USER_PASSWORD = "test-user-id";

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private CommunityService communityService;
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
    User resultUser = new User(request.getName(), request.getUserId(), request.getEmail(), request.getEncryptedPassword());
    UserDto response = new UserDto(resultUser.getId(), resultUser.getUserId(), resultUser.getName(), resultUser.getEmail(), null, resultUser.getEncryptedPassword(), new HashSet<>());

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
    Optional<UserDto> createdUserDto = userService.createUser(request);

    // then
    assertTrue(createdUserDto.isPresent());
    assertEquals(createdUserDto.get(), response);
    assertEquals(createdUserDto.get().getCommunityIds().size(), 0);
    verify(userRepository).findByEmail(request.getEmail());
    verify(passwordEncoder).encode(request.getPassword());
    verify(userRepository).save(resultUser);
  }

  @Test
  void createUserEmailExists() {
    // given
    UserDto request = getDefaultUserDtoRequest();
    User user = new User(request.getName(), request.getUserId(), request.getEmail(), request.getEncryptedPassword());

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
    User user = new User(userDto.getName(), userDto.getUserId(), userDto.getEmail(), userDto.getEncryptedPassword());

    given(userRepository.findByUserId(USER_ID))
        .willReturn(user);
    given(communityService.listAll())
        .willReturn(new HashSet<>());
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> createdUserDto = userService.getUserDetails(USER_ID);

    // then
    assertTrue(createdUserDto.isPresent());
    assertEquals(createdUserDto.get(), userDto);
    assertEquals(createdUserDto.get().getCommunityIds().size(), 0);
    verify(userRepository).findByUserId(USER_ID);
    verify(communityService).listAll();
  }

  @Test
  void getUserDetailsSuccessWithCommunityIds() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = new User(userDto.getName(), userDto.getUserId(), userDto.getEmail(), userDto.getEncryptedPassword());

    CommunityAdmin communityUserAdmin = getAdminFromUser();
    Community firstCommunity = createCommunityWithUserAdmin(communityUserAdmin);
    Community secCommunity = createCommunityWithUserAdmin(communityUserAdmin);

    Set<Community> communities = new HashSet<Community>(){{
      add(firstCommunity);
      add(secCommunity);
    }};
    Set<String> communitiesIds = communities.stream().map(comm -> comm.getCommunityId()).collect(Collectors.toSet());

    given(userRepository.findByUserId(USER_ID))
        .willReturn(user);
    given(communityService.listAll())
        .willReturn(communities);
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> createdUserDto = userService.getUserDetails(USER_ID);

    // then
    assertTrue(createdUserDto.isPresent());
    assertEquals(createdUserDto.get(), userDto);
    assertEquals(createdUserDto.get().getCommunityIds(), communitiesIds);
    verify(userRepository).findByUserId(USER_ID);
    verify(communityService).listAll();
  }

  @Test
  void getUserDetailsNotFound() {
    // given
    given(userRepository.findByUserId(USER_ID))
        .willReturn(null);
    given(communityService.listAll())
        .willReturn(new HashSet<>());
    given(userMapper.userToUserDto(any()))
        .willReturn(null);

    // when
    Optional<UserDto> createdUserDto = userService.getUserDetails(USER_ID);

    // then
    assertFalse(createdUserDto.isPresent());
    verify(userRepository).findByUserId(USER_ID);
  }

  private Community createCommunityWithUserAdmin(CommunityAdmin communityUserAdmin) {
    Community community = new Community();
    community.getAdmins().add(communityUserAdmin);
    return community;
  }

  private UserDto getDefaultUserDtoRequest() {
    return new UserDto(null, USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, null, new HashSet<>());
  }

  private CommunityAdmin getAdminFromUser() {
    CommunityAdmin communityAdmin = new CommunityAdmin();
    communityAdmin.setAdminId(USER_ID);
    return communityAdmin;
  }

}