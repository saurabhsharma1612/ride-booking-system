package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.RideRequest;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.enums.RideRequestStatus;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.repository.RideRepository;
import com.saurabh.ridebooking.repository.RideRequestRepository;
import com.saurabh.ridebooking.services.RideService;
import com.saurabh.ridebooking.strategies.DriverMatchingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideRequestRepository rideRequestRepository;
    private final DriverRepository driverRepository;
    private final DriverMatchingStrategy driverMatchingStrategy;

    private final SecureRandom secureRandom = new SecureRandom();

    public RideServiceImpl(
            RideRepository rideRepository,
            RideRequestRepository rideRequestRepository,
            DriverRepository driverRepository,
            DriverMatchingStrategy driverMatchingStrategy
    ) {
        this.rideRepository = rideRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.driverRepository = driverRepository;
        this.driverMatchingStrategy = driverMatchingStrategy;
    }

    @Override
    @Transactional
    public Ride matchWithDrivers(RideRequestDto rideRequestDto) {

        if (rideRequestDto.getId() == null) {
            throw new IllegalArgumentException(
                    "Ride request id is required"
            );
        }

        RideRequest rideRequest =
                rideRequestRepository.findById(
                        rideRequestDto.getId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Ride request not found"
                        )
                );

        if (rideRequest.getRideRequestStatus()
                != RideRequestStatus.PENDING) {
            return null;
        }

        var drivers =
                driverMatchingStrategy.findMatchingDriver(
                        rideRequestDto
                );

        if (drivers.isEmpty()) {
            return null;
        }

        Driver driver = drivers.get(0);

        return createNewRide(
                rideRequest,
                driver
        );
    }

    @Override
    @Transactional
    public Ride createNewRide(
            RideRequest rideRequest,
            Driver driver
    ) {

        Ride ride = new Ride();

        ride.setPickupLocation(
                rideRequest.getPickupLocation()
        );

        ride.setDropOffLocation(
                rideRequest.getDropOffLocation()
        );

        ride.setRider(
                rideRequest.getRider()
        );

        ride.setDriver(driver);

        ride.setPaymentMethod(
                rideRequest.getPaymentMethod()
        );

        ride.setFare(
                rideRequest.getFare()
        );

        ride.setOtp(
                generateOtp()
        );

        ride.setRideStatus(
                RideStatus.CONFIRMED
        );

        Ride savedRide = rideRepository.save(ride);

        rideRequest.setRideRequestStatus(
                RideRequestStatus.CONFIRMED
        );

        rideRequestRepository.save(rideRequest);

        driver.setAvailable(false);
        driverRepository.save(driver);

        return savedRide;
    }

    @Override
    @Transactional
    public Ride updateRideStatus(
            Ride ride,
            RideStatus rideStatus
    ) {

        ride.setRideStatus(rideStatus);

        if (rideStatus == RideStatus.ONGOING) {
            ride.setStartedAt(
                    java.time.LocalDateTime.now()
            );
        }

        if (rideStatus == RideStatus.ENDED) {
            ride.setEndedAt(
                    java.time.LocalDateTime.now()
            );
        }

        return rideRepository.save(ride);
    }

    @Override
    public Ride getRideById(Long rideId) {

        return rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Ride not found"
                        )
                );
    }

    @Override
    public Page<Ride> getAllRidesOfRider(
            Rider rider,
            PageRequest pageRequest
    ) {

        return rideRepository.findByRider(
                rider,
                pageRequest
        );
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(
            Driver driver,
            PageRequest pageRequest
    ) {

        return rideRepository.findByDriver(
                driver,
                pageRequest
        );
    }

    private String generateOtp() {

        int otp = 100000 + secureRandom.nextInt(900000);

        return String.valueOf(otp);
    }
}