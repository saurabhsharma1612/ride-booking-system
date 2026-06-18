package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.SignupDto;
import com.saurabh.ridebooking.dto.UserDto;
import com.saurabh.ridebooking.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    public UserDto signup(SignupDto signupDto) {
        return null;
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
