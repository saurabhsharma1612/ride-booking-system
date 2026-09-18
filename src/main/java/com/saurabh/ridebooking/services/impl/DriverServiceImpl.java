package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.PageResponseDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RiderDto;
import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.exceptions.ForbiddenException;
import com.saurabh.ridebooking.exceptions.ResourceNotFoundException;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.repository.UserRepository;
import com.saurabh.ridebooking.services.DriverService;
import com.saurabh.ridebooking.services.RatingService;
import com.saurabh.ridebooking.services.RideService;
import com.saurabh.ridebooking.utils.GeometryUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saurabh.ridebooking.utils.PaginationUtils;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RideService rideService;
    private final RatingService ratingService;

    public DriverServiceImpl(
            DriverRepository driverRepository,
            UserRepository userRepository,
            ModelMapper modelMapper,
            RideService rideService, RatingService ratingService
    ) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.rideService = rideService;
        this.ratingService = ratingService;
    }

    private Driver getCurrentDriver() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
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
            throw new ForbiddenException(
                    "Ride is not assigned to this driver"
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
            throw new ForbiddenException(
                    "Ride is not assigned to this driver"
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
    public RiderDto rateRider(
            Long rideId,
            Integer rating
    ) {

        Driver driver = getCurrentDriver();

        Rider rider =
                ratingService.rateRider(
                        rideId,
                        driver.getUser(),
                        rating
                );

        return modelMapper.map(
                rider,
                RiderDto.class
        );
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
    public PageResponseDto<RideDto> getAllMyRides(
            int page,
            int size
    ) {

        Driver driver = getCurrentDriver();

        return PageResponseDto.from(
                rideService.getAllRidesOfDriver(
                        driver,
                        PaginationUtils.createPageRequest(page, size)
                ).map(this::toRideDto)
        );
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
