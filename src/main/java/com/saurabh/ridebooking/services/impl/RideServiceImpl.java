package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.dto.RiderDto;
import com.saurabh.ridebooking.services.RiderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideServiceImpl implements RiderService {
    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        return null;
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyRides() {
        return List.of();
    }
}
