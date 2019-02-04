package com.example.auth.demo.controller;

import com.example.auth.demo.model.Hello;
import com.example.auth.demo.model.HelloResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class MainController {

    @GetMapping("hello")
    public ResponseEntity<?> hello() {
        return new HelloResponse(new Hello("Hello World"), HttpStatus.OK);
    }
}
