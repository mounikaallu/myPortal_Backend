package com.portal.controller;

import com.portal.service.LoginResponse;
import com.portal.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request.getEmail(), request.getPassword());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
