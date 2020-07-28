package com.myhome.controllers.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.myhome.controllers.request.CreateUserRequest;
import com.myhome.controllers.request.LoginUserRequest;
import com.myhome.controllers.response.CreateUserResponse;
import com.myhome.controllers.response.GetUserDetailsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerIntegrationTest extends ControllerIntegrationTestBase {

  private static final String testUserName = "Test User";
  private static final String testUserEmail = "testuser@myhome.com";
  private static final String testUserPassword = "testpassword";

  private static CreateUserRequest getUserRequest() {
    return new CreateUserRequest(testUserName, testUserEmail, testUserPassword);
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
    response = sendRequest(HttpMethod.GET, String.format("users/%s", createUserResponse.getUserId()), null);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    GetUserDetailsResponse.User getUserDetailsResponse = readValue(response,GetUserDetailsResponse.User.class);
    assertEquals(testUserName,getUserDetailsResponse.getName());
    assertEquals(testUserEmail,getUserDetailsResponse.getEmail());
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
    sendRequest(HttpMethod.POST, "users", getUserRequest());
    updateJwtToken(new LoginUserRequest(testUserEmail, testUserPassword));
    ResponseEntity<String> response = sendRequest(HttpMethod.GET, "users", null);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    GetUserDetailsResponse getUserDetailsResponse = readValue(response, GetUserDetailsResponse.class);
    assertNotNull(getUserDetailsResponse.getUsers());
  }
}