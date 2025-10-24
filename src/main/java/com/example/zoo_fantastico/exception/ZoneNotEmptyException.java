package com.example.zoo_fantastico.exception;

public class ZoneNotEmptyException extends RuntimeException {
    public ZoneNotEmptyException(String message) {
        super(message);
    }
}