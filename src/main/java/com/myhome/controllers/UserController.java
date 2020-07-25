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

package com.myhome.controllers;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.mapper.UserApiMapper;
import com.myhome.controllers.request.CreateUserRequest;
import com.myhome.controllers.response.CreateUserResponse;
import com.myhome.controllers.response.GetUserDetailsResponse;
import com.myhome.domain.User;
import com.myhome.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for facilitating user actions.
 */
@RestController
@Slf4j
public class UserController {
  private final UserService userService;
  private final UserApiMapper userApiMapper;

  public UserController(UserService userService,
      UserApiMapper userApiMapper) {
    this.userService = userService;
    this.userApiMapper = userApiMapper;
  }

  @Operation(description = "Create a new user")
  @PostMapping(
      path = "/users",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<CreateUserResponse> signUp(@Valid @RequestBody CreateUserRequest request) {
    log.trace("Received SignUp request");
    UserDto requestUserDto = userApiMapper.createUserRequestToUserDto(request);
    UserDto createdUserDto = userService.createUser(requestUserDto);
    CreateUserResponse createdUserResponse =
        userApiMapper.userDtoToCreateUserResponse(createdUserDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUserResponse);
  }

  /**
   * Return all registered users in the application
   *
   * @param sort can take either "asc" or "desc" as a value. API returns BAD_REQUEST if param is
   *             invalid.
   * @return All registered users in the application
   */
  @GetMapping(path = "/users",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<GetUserDetailsResponse> listAllUsers(
      @PathVariable(required = false) String sort) {
    log.trace("Received request to list all users");

    if (sort != null) {
      if (!sort.contentEquals("asc") || !sort.contentEquals("desc")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
    }
    if (sort == null) {
      sort = "asc";
    }
    List<User> userDetails = userService.listAll(sort);
    List<GetUserDetailsResponse.User> userDetailsResponse =
        userApiMapper.userSetToRestApiResponseUserSet(userDetails);

    GetUserDetailsResponse response = new GetUserDetailsResponse();
    response.getUsers().addAll(userDetailsResponse);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "Get details of a user given userId"
      , responses = {@ApiResponse(responseCode = "404", description = "If userId is invalid"),
      @ApiResponse(responseCode = "200",
          description = "If userId is valid. Response body has the details ")})
  @GetMapping(path = "/users/{userId}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<GetUserDetailsResponse.User> getUserDetails(
      @Valid @PathVariable @NonNull String userId) {
    log.trace("Received request to get details of user with Id[{}]", userId);

    UserDto userDto = new UserDto();
    userDto.setUserId(userId);
    Optional<UserDto> userDetails = userService.getUserDetails(userDto);
    if (!userDetails.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    GetUserDetailsResponse.User response =
        userApiMapper.userDtoToGetUserDetailsResponse(userDetails.get());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
