package com.saurabh.ridebooking.controllers;

import com.saurabh.ridebooking.dto.SignupDto;
import com.saurabh.ridebooking.dto.UserDto;
import com.saurabh.ridebooking.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
        UserDto userDto = authService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}