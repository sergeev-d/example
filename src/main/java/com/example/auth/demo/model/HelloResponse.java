package com.example.auth.demo.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HelloResponse extends ResponseEntity<Hello> {

    public HelloResponse(Hello body, HttpStatus status) {
        super(body, status);
    }
}
