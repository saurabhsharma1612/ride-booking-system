package com.saurabh.ridebooking.services;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.SignupDto;
import com.saurabh.ridebooking.dto.UserDto;

public interface AuthService {

    String login(String email, String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId);
}
