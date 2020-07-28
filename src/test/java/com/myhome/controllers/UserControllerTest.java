package com.myhome.controllers;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.mapper.UserApiMapper;
import com.myhome.controllers.request.CreateUserRequest;
import com.myhome.controllers.response.CreateUserResponse;
import com.myhome.controllers.response.GetUserDetailsResponse;
import com.myhome.domain.User;
import com.myhome.services.UserService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class UserControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private UserApiMapper userApiMapper;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void shouldSignUpSuccessful() {
    // given
    CreateUserRequest request = new CreateUserRequest("name", "email", "password");
    UserDto userDto = new UserDto();
    userDto.setName("name");
    userDto.setEmail("email");
    userDto.setPassword("password");
    CreateUserResponse createUserResponse = new CreateUserResponse("1", "name", "email");

    given(userApiMapper.createUserRequestToUserDto(request))
        .willReturn(userDto);
    given(userService.createUser(userDto))
        .willReturn(userDto);
    given(userApiMapper.userDtoToCreateUserResponse(userDto))
        .willReturn(createUserResponse);

    // when
    ResponseEntity<CreateUserResponse> responseEntity = userController.signUp(request);

    // then
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals(createUserResponse, responseEntity.getBody());
    verify(userApiMapper).createUserRequestToUserDto(request);
    verify(userService).createUser(userDto);
    verify(userApiMapper).userDtoToCreateUserResponse(userDto);
  }

  @Test
  void shouldListUsersSuccess() {
    // given
    Integer limit = 150;
    Integer start = 50;

    Set<User> users = new HashSet<>();
    users.add(new User("name", "1", "email", "hash"));

    Set<GetUserDetailsResponse.User> responseUsers = new HashSet<>();
    responseUsers.add(
        new GetUserDetailsResponse.User(
            "1",
            "name",
            "email",
            Collections.emptySet()
        )
    );
    GetUserDetailsResponse expectedResponse = new GetUserDetailsResponse(responseUsers);

    given(userService.listAll(limit, start))
        .willReturn(users);
    given(userApiMapper.userSetToRestApiResponseUserSet(users))
        .willReturn(responseUsers);

    // when
    ResponseEntity<GetUserDetailsResponse> responseEntity =
        userController.listAllUsers(limit, start);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedResponse, responseEntity.getBody());
    verify(userService).listAll(limit, start);
    verify(userApiMapper).userSetToRestApiResponseUserSet(users);
  }

  @Test
  void shouldGetUserDetailsSuccessWithNoResults() {
    // given
    String userId = "1";
    given(userService.getUserDetails(userId))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetUserDetailsResponse.User> response = userController.getUserDetails(userId);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(userService).getUserDetails(userId);
    verifyNoInteractions(userApiMapper);
  }

  @Test
  void shouldGetUserDetailsSuccessWithResults() {
    // given
    String userId = "1";
    UserDto userDto = new UserDto();
    userDto.setUserId(userId);
    GetUserDetailsResponse.User expectedResponse = new GetUserDetailsResponse.User(
        "1",
        "name",
        "email",
        Collections.emptySet()
    );
    given(userService.getUserDetails(userId))
        .willReturn(Optional.of(userDto));
    given(userApiMapper.userDtoToGetUserDetailsResponse(userDto))
        .willReturn(expectedResponse);

    // when
    ResponseEntity<GetUserDetailsResponse.User> response = userController.getUserDetails(userId);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
    verify(userService).getUserDetails(userId);
    verify(userApiMapper).userDtoToGetUserDetailsResponse(userDto);
  }
}