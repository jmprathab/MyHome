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

package com.myhome.controllers.integration;

import com.myhome.controllers.HealthCheckController;
import com.myhome.model.CreateUserRequest;
import com.myhome.controllers.request.LoginUserRequest;
import com.myhome.controllers.response.CreateUserResponse;
import com.myhome.model.GetUserDetailsResponse;
import com.myhome.model.GetUserDetailsResponseUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControllerIntegrationTest extends ControllerIntegrationTestBase {

  private static final String testUserName = "Test User";
  private static final String testUserEmail = "testuser@myhome.com";
  private static final String testUserPassword = "testpassword";

  private static CreateUserRequest getUserRequest() {
    return new CreateUserRequest()
        .name(testUserName)
        .email(testUserEmail)
        .password(testUserPassword);
  }

  @Test
  @DisplayName("test POST /users signUp positive & GET /users specific getUserDetails() positive")
  void getUserDetailsPositive() {
    CreateUserRequest createUserRequest = getUserRequest();
    ResponseEntity<String> response = sendRequest(HttpMethod.POST, "users", createUserRequest);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    CreateUserResponse createUserResponse = readValue(response, CreateUserResponse.class);
    assertEquals(testUserName, createUserResponse.getName());
    assertEquals(testUserEmail, createUserResponse.getEmail());
    updateJwtToken(new LoginUserRequest(testUserEmail, testUserPassword));
    response =
        sendRequest(HttpMethod.GET, String.format("users/%s", createUserResponse.getUserId()),
            null);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    GetUserDetailsResponseUser getUserDetailsResponse =
        readValue(response, GetUserDetailsResponseUser.class);
    assertEquals(testUserName, getUserDetailsResponse.getName());
    assertEquals(testUserEmail, getUserDetailsResponse.getEmail());
  }

  @Test
  @DisplayName("test post /users signUp negative missing name")
  void signUpNegative() {
    CreateUserRequest createUserRequest = getUserRequest();
    createUserRequest.setName(null);
    ResponseEntity<String> response = sendRequest(HttpMethod.POST, "users", createUserRequest);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("test get /users listAllUsers() positive")
  void listAllUsersPositive() {
    // given
    sendRequest(HttpMethod.POST, "users", getUserRequest());
    updateJwtToken(new LoginUserRequest(testUserEmail, testUserPassword));

    // when
    ResponseEntity<String> response = sendRequest(HttpMethod.GET, "users", null);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    GetUserDetailsResponse getUserDetailsResponse =
        readValue(response, GetUserDetailsResponse.class);
    assertNotNull(getUserDetailsResponse.getUsers());
  }

  @Test
  @DisplayName("HealthCheckControllerIntegrationTest statusCheck()")
  void statusCheck() throws Exception {
    mockMvc.perform(get("/status"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(HealthCheckController.STATUS_HEALTHY)));
  }
}