package com.saurabh.ridebooking.strategies;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.entities.Driver;

import java.util.List;

public interface DriverMatchingStrategy {

     List<Driver> findMatchingDriver(RideRequestDto rideRequestDto);

}
