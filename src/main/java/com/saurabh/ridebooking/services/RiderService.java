package com.saurabh.ridebooking.services;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.dto.RiderDto;
import com.saurabh.ridebooking.entities.RideRequest;

import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateRider(Long rideId, Integer rating);

    RiderDto getMyProfile();

    List<RideDto> getAllMyRides();
}
