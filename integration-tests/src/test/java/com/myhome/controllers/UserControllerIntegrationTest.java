package com.myhome.controllers;

import com.myhome.MyHomeServiceApplication;
import com.myhome.domain.User;
import com.myhome.model.CreateUserRequest;
import com.myhome.repositories.UserRepository;
import io.restassured.http.ContentType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MyHomeServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserControllerIntegrationTest {

  private static final String TEST_NAME = "name";
  private static final String TEST_EMAIL = "email@mail.com";
  private static final String TEST_PASSWORD = "password";

  @Value("${api.registration.url.path}")
  private String registrationPath;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void shouldSignUpSuccessful() {
    // Given a request object
    CreateUserRequest requestBody = new CreateUserRequest()
        .name(TEST_NAME)
        .email(TEST_EMAIL)
        .password(TEST_PASSWORD);

    //@formatter:off
    String responseId =

    // And a request is set up against the locally running service
    given()
        .webAppContextSetup(webApplicationContext)
        .contentType(ContentType.JSON)
        .body(requestBody)

    // When the request is made
    .when()
        .post(registrationPath)

    // Then validation failures should be logged
    .then()
        .log().ifValidationFails()

    // And the response should match
    .and()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body("name", equalTo(TEST_NAME))
        .body("email", equalTo(TEST_EMAIL))
        .body("userId", not(emptyOrNullString()))
    .and()
        .extract().path("userId");
    //@formatter:on

    // And the returned user ID should be stored in the database
    Optional<User> user = userRepository.findByUserId(responseId);
    assertTrue(user.isPresent());

    // And the corresponding values should match the input
    assertThat(user.get().getName(), equalTo(TEST_NAME));
    assertThat(user.get().getEmail(), equalTo(TEST_EMAIL));
  }
}
