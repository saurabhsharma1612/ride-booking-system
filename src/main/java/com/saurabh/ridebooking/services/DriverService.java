package com.saurabh.ridebooking.services;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.PageResponseDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RiderDto;

public interface DriverService {

    RideDto acceptRide(Long rideId);

    RideDto cancelRide(Long rideId);

    RideDto startRide(Long rideId);

    RideDto endRide(Long rideId);

    RiderDto rateRider(Long rideId, Integer rating);

    DriverDto getMyProfile();

    PageResponseDto<RideDto> getAllMyRides(int page, int size);
}
