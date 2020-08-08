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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.MyHomeServiceApplication;
import com.myhome.controllers.request.LoginUserRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Optional.ofNullable;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
    MyHomeServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class ControllerIntegrationTestBase {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  private TestRestTemplate client;
  @LocalServerPort
  private int randomPort;
  private String jwtToken;

  protected <T> ResponseEntity<String> sendRequest(HttpMethod httpMethod, String urlParams,
      T body) {
    String url = String.format("http://localhost:%d/%s", randomPort, urlParams);
    try {
      return client.exchange(new URI(url), httpMethod,
          new HttpEntity<>(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body),
              getHttpEntityHeaders()), String.class);
    } catch (URISyntaxException | JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  protected <T> T readValue(ResponseEntity<String> response, Class<T> type) {
    try {
      return objectMapper.readValue(response.getBody(), type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  protected HttpHeaders getHttpEntityHeaders() {
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
