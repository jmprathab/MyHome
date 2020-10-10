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
