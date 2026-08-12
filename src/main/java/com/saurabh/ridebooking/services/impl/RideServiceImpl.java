package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.RideRequest;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.services.RideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RideServiceImpl implements RideService {

    @Override
    public Ride getRideById(Long rideId) {
        return null;
    }

    @Override
    public void matchWithDrivers(RideRequestDto rideRequestDto) {

    }

    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        return null;
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        return null;
    }

    @Override
    public Page<Ride> getAllRidesOfRider(
            Rider rider,
            PageRequest pageRequest
    ) {
        return null;
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(
            Driver driver,
            PageRequest pageRequest
    ) {
        return null;
    }
}