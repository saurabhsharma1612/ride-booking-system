package com.saurabh.ridebooking.strategies.impl;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.strategies.DriverMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Driver> findMatchingDriver(RideRequestDto rideRequestDto) {
        return List.of();
    }
}
