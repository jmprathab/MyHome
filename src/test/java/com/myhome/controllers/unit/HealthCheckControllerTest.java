package com.myhome.controllers.unit;

import com.myhome.controllers.HealthCheckController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckControllerTest {

  @InjectMocks
  private HealthCheckController healthCheckController;

  @BeforeEach
  void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void shouldReturnHealthySuccessfully() {
    // given
    String healthyMessage = "Application is healthy";

    // when
    ResponseEntity<String> responseEntity = healthCheckController.statusCheck();

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(healthyMessage, responseEntity.getBody());
  }
}
