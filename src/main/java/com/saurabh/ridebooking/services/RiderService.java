package com.saurabh.ridebooking.services;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.PageResponseDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.dto.RiderDto;
import com.saurabh.ridebooking.entities.RideRequest;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    PageResponseDto<RideDto> getAllMyRides(int page, int size);
}
