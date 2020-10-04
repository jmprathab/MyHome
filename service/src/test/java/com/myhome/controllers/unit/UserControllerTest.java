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

package com.myhome.controllers.unit;

import com.myhome.controllers.UserController;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class UserControllerTest {

  private static final String TEST_ID = "1";
  private static final String TEST_NAME = "name";
  private static final String TEST_EMAIL = "email@mail.com";
  private static final String TEST_PASSWORD = "password";

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
    CreateUserRequest request = new CreateUserRequest(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    UserDto userDto = UserDto.builder()
        .name(TEST_NAME)
        .email(TEST_EMAIL)
        .password(TEST_PASSWORD)
        .build();
    CreateUserResponse createUserResponse = new CreateUserResponse(TEST_ID, TEST_NAME, TEST_EMAIL);

    given(userApiMapper.createUserRequestToUserDto(request))
        .willReturn(userDto);
    given(userService.createUser(userDto))
        .willReturn(Optional.of(userDto));
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
    int limit = 150;
    int start = 50;
    PageRequest pageRequest = PageRequest.of(start, limit);

    Set<User> users = new HashSet<>();
    users.add(new User(TEST_NAME, TEST_ID, TEST_EMAIL, TEST_PASSWORD, new HashSet<>()));

    Set<GetUserDetailsResponse.User> responseUsers = new HashSet<>();
    responseUsers.add(
        new GetUserDetailsResponse.User(
            TEST_ID,
            TEST_NAME,
            TEST_EMAIL,
            Collections.emptySet()
        )
    );
    GetUserDetailsResponse expectedResponse = new GetUserDetailsResponse(responseUsers);

    given(userService.listAll(pageRequest))
        .willReturn(users);
    given(userApiMapper.userSetToRestApiResponseUserSet(users))
        .willReturn(responseUsers);

    // when
    ResponseEntity<GetUserDetailsResponse> responseEntity =
        userController.listAllUsers(pageRequest);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedResponse, responseEntity.getBody());
    verify(userService).listAll(pageRequest);
    verify(userApiMapper).userSetToRestApiResponseUserSet(users);
  }

  @Test
  void shouldGetUserDetailsSuccessWithNoResults() {
    // given
    String userId = TEST_ID;
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
    String userId = TEST_ID;
    UserDto userDto = UserDto.builder()
        .userId(userId)
        .build();
    GetUserDetailsResponse.User expectedResponse = new GetUserDetailsResponse.User(
        TEST_ID,
        TEST_NAME,
        TEST_EMAIL,
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