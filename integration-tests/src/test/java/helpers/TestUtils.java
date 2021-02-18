package helpers;

import com.myhome.controllers.request.LoginUserRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestUtils {

  public static class Login {

    private static final String LOGIN_EMAIL = "test@test.com";
    private static final String LOGIN_PASSWORD = "testtest";

    public static ResponseEntity<Void> performLogin(TestRestTemplate testRestTemplate,
        Environment env) {
      ResponseEntity<Void> responseEntity = testRestTemplate.postForEntity(
          env.getProperty("api.login.url.path", "/users/login"),
          new LoginUserRequest(LOGIN_EMAIL, LOGIN_PASSWORD),
          Void.class);

      if (responseEntity.getStatusCode() != HttpStatus.OK) {
        throw new IllegalStateException("Login didn't respond as expected");
      }

      return responseEntity;
    }

    public static String getTokenFromLoginResponse(ResponseEntity<Void> responseEntity) {
      return responseEntity.getHeaders()
          .getOrEmpty("token")
          .stream()
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Login didn't respond as expected"));
    }

    public static String getUserIdFromLoginResponse(ResponseEntity<Void> responseEntity) {
      return responseEntity.getHeaders()
          .getOrEmpty("userId")
          .stream()
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Login didn't respond as expected"));
    }
  }
}
