package com.myhome.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @Operation(description = "Endpoint to know if the server is healthy")
  @GetMapping("/status")
  public String statusCheck() {
    return "Application is healthy";
  }
}
