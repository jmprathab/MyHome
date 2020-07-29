package com.myhome.controllers.integration;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.MyHomeServiceApplication;
import com.myhome.controllers.request.LoginUserRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
    MyHomeServiceApplication.class})
public class ControllerIntegrationTestBase {

  @Autowired
  private TestRestTemplate client;
  @Autowired
  ObjectMapper objectMapper;
  @LocalServerPort
  private int randomPort;
  private String jwtToken;

  protected <T> ResponseEntity<String> sendRequest(HttpMethod httpMethod, String urlParams, T body){
    String url = String.format("http://localhost:%d/%s", randomPort, urlParams);
    try{
      return client.exchange(new URI(url), httpMethod,
          new HttpEntity<>(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body),
              getHttpEntityHeaders()), String.class);
    } catch (URISyntaxException | JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  protected <T> T readValue (ResponseEntity<String> response, Class<T> type){
    try{
      return objectMapper.readValue(response.getBody(), type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private HttpHeaders getHttpEntityHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    ofNullable(jwtToken).ifPresent(headers::setBearerAuth);
    return headers;
  }

  public void updateJwtToken(LoginUserRequest loginUserRequest) {
    jwtToken = Objects.requireNonNull(
        sendRequest(HttpMethod.POST, "users/login", loginUserRequest).getHeaders().get("token"))
        .get(0);
  }
}
