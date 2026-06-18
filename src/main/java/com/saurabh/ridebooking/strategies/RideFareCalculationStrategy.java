package com.saurabh.ridebooking.strategies;

import com.saurabh.ridebooking.dto.RideRequestDto;

public interface RideFareCalculationStrategy {

    double calculateFare(RideRequestDto rideRequestDto);

}
