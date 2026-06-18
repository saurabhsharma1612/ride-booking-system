package com.saurabh.ridebooking.strategies.impl;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.strategies.RideFareCalculationStrategy;
import org.springframework.stereotype.Service;

@Service
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequestDto rideRequestDto) {
        return 0;
    }
}
