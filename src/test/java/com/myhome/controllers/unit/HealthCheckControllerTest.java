package com.myhome.controllers.unit;

import com.myhome.controllers.HealthCheckController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckControllerTest {

  @InjectMocks
  HealthCheckController healthCheckController;

  @BeforeEach
  void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void shouldReturnHealthySuccessfully() {
    assertEquals("Application is healthy", healthCheckController.statusCheck());
  }
}
