package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RiderDto;
import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.repository.RideRepository;
import com.saurabh.ridebooking.repository.UserRepository;
import com.saurabh.ridebooking.services.DriverService;
import com.saurabh.ridebooking.services.RideService;
import com.saurabh.ridebooking.utils.GeometryUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;
    private final RideService rideService;

    public DriverServiceImpl(
            DriverRepository driverRepository,
            UserRepository userRepository,
            RideRepository rideRepository,
            ModelMapper modelMapper,
            RideService rideService
    ) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.modelMapper = modelMapper;
        this.rideService = rideService;
    }

    private Driver getCurrentDriver() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        return driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Driver profile not found"
                        )
                );
    }

    @Override
    public RideDto acceptRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    @Transactional
    public RideDto startRide(Long rideId) {

        Driver driver = getCurrentDriver();

        Ride ride = rideService.getRideById(rideId);

        if (ride.getDriver() == null
                || !ride.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException(
                    "Ride is not assigned to this driver"
            );
        }

        if (ride.getRideStatus() != RideStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only a confirmed ride can be started"
            );
        }

        Ride updatedRide =
                rideService.updateRideStatus(
                        ride,
                        RideStatus.ONGOING
                );

        return toRideDto(updatedRide);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {

        Driver driver = getCurrentDriver();

        Ride ride = rideService.getRideById(rideId);

        if (ride.getDriver() == null
                || !ride.getDriver().getId().equals(driver.getId())) {
            throw new IllegalArgumentException(
                    "Ride is not assigned to this driver"
            );
        }

        if (ride.getRideStatus() != RideStatus.ONGOING) {
            throw new IllegalStateException(
                    "Only an ongoing ride can be ended"
            );
        }

        Ride updatedRide =
                rideService.updateRideStatus(
                        ride,
                        RideStatus.ENDED
                );

        driver.setAvailable(true);
        driverRepository.save(driver);

        return toRideDto(updatedRide);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public DriverDto getMyProfile() {

        Driver driver = getCurrentDriver();

        return modelMapper.map(
                driver,
                DriverDto.class
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideDto> getAllMyRides() {

        Driver driver = getCurrentDriver();

        List<Ride> rides =
                rideRepository.findByDriver(
                        driver,
                        PageRequest.of(0, 100)
                ).getContent();

        return rides.stream()
                .map(this::toRideDto)
                .toList();
    }

    private RideDto toRideDto(Ride ride) {

        RideDto rideDto =
                modelMapper.map(
                        ride,
                        RideDto.class
                );

        rideDto.setPickupLocation(
                GeometryUtils.toLocationDto(
                        ride.getPickupLocation()
                )
        );

        rideDto.setDropLocation(
                GeometryUtils.toLocationDto(
                        ride.getDropOffLocation()
                )
        );

        return rideDto;
    }
}