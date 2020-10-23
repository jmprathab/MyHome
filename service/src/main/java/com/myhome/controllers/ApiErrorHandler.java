package com.myhome.controllers;

import com.myhome.controllers.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiErrorHandler {

  @ExceptionHandler(value = { Exception.class })
  protected ResponseEntity<ExceptionResponse> handleException(RuntimeException exception) {
    String[] splitMessage = exception.getMessage().split(":");
    if (splitMessage.length == 2) {
      return new ResponseEntity<>(new ExceptionResponse(splitMessage[0]), HttpStatus.resolve(Integer.valueOf(splitMessage[1])));
    }
    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }
}

