package com.example.auth.demo.controller;

import com.example.auth.demo.model.Hello;
import com.example.auth.demo.model.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader("x-client") String client){
        return new ResponseEntity<>(new Hello(loginRequest.getNameOrEmail()), HttpStatus.UNAUTHORIZED);
    }
}
