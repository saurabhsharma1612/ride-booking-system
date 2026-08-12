package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.configs.JwtService;
import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.SignupDto;
import com.saurabh.ridebooking.dto.UserDto;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.Wallet;
import com.saurabh.ridebooking.entities.enums.Role;
import com.saurabh.ridebooking.repository.RiderRepository;
import com.saurabh.ridebooking.repository.UserRepository;
import com.saurabh.ridebooking.repository.WalletRepository;
import com.saurabh.ridebooking.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RiderRepository riderRepository,
            WalletRepository walletRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.riderRepository = riderRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Override
    public UserDto signup(SignupDto signupDto) {

        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setName(signupDto.getName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRoles(Set.of(Role.USER));

        User savedUser = userRepository.save(user);

        Rider rider = new Rider();
        rider.setUser(savedUser);
        rider.setRating(0.0);
        riderRepository.save(rider);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);

        UserDto userDto = new UserDto();
        userDto.setName(savedUser.getName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setRoles(savedUser.getRoles());

        return userDto;
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }

    @Override
    public String login(String email, String password) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                password
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        return jwtService.generateAccessToken(userDetails);
    }

}