package com.example.zoo_fantastico.controller;

import com.example.zoo_fantastico.exception.ZoneNotEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ZoneNotEmptyException.class)
    public ResponseEntity<String> handleZoneNotEmptyException(ZoneNotEmptyException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ex.getMessage());
    }
}