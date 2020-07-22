package com.myhome.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.MyHomeServiceApplication;
import java.net.URI;
import java.net.URISyntaxException;
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
public class ControllerTestBase {

  @Autowired
  private TestRestTemplate client;
  @Autowired
  ObjectMapper objectMapper;
  @LocalServerPort
  private int randomPort;
  private static final String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyNGI0Mzc5MS02YWY0LTQ5MGYtODYzOS00ZDUyYmJhYjQzNTMiLCJleHAiOjE1OTYyODI4ODl9.ohTsY67FhFp9p79h_rnenHnU6IRxO7-5PojMAhwqNLOM5qlVNSHbpWj-lVTk_fRzk4X-oDcB-o8RP1LZXBI5BQ";

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
    headers.setBearerAuth(jwtToken);
    return headers;
  }
}
