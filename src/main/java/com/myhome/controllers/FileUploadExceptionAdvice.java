package com.myhome.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.util.HashMap;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new HashMap<String, String>(){{
            put("message", "File size exceeds limit!");
        }});
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new HashMap<String, String>(){{
            put("message", "Something go wrong with document saving!");
        }});
    }
}

