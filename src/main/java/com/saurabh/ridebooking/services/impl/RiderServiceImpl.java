package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.dto.*;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.RideRequest;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.enums.RideRequestStatus;
import com.saurabh.ridebooking.repository.RideRequestRepository;
import com.saurabh.ridebooking.repository.RiderRepository;
import com.saurabh.ridebooking.repository.UserRepository;
import com.saurabh.ridebooking.services.RideService;
import com.saurabh.ridebooking.services.RiderService;
import com.saurabh.ridebooking.strategies.RideFareCalculationStrategy;
import com.saurabh.ridebooking.utils.GeometryUtils;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
    private final UserRepository userRepository;
    private final RideRequestRepository rideRequestRepository;
    private final ModelMapper modelMapper;
    private final RideFareCalculationStrategy fareCalculationStrategy;
    private final GeometryFactory geometryFactory;
    private final RideService rideService;

    public RiderServiceImpl(
            RiderRepository riderRepository,
            UserRepository userRepository,
            RideRequestRepository rideRequestRepository,
            ModelMapper modelMapper,
            RideFareCalculationStrategy fareCalculationStrategy,
            GeometryFactory geometryFactory, RideService rideService
    ) {
        this.riderRepository = riderRepository;
        this.userRepository = userRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.modelMapper = modelMapper;
        this.fareCalculationStrategy = fareCalculationStrategy;
        this.geometryFactory = geometryFactory;
        this.rideService = rideService;
    }

    @Override
    @Transactional
    public RideRequestDto requestRide(
            RideRequestDto rideRequestDto
    ) {

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

        Rider rider = riderRepository.findByUser(user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Rider profile not found"
                        )
                );

        RideRequest rideRequest =
                modelMapper.map(
                        rideRequestDto,
                        RideRequest.class
                );

        rideRequest.setPickupLocation(
                GeometryUtils.toPoint(
                        rideRequestDto.getPickupLocation(),
                        geometryFactory
                )
        );

        rideRequest.setDropOffLocation(
                GeometryUtils.toPoint(
                        rideRequestDto.getDropLocation(),
                        geometryFactory
                )
        );

        rideRequest.setRider(rider);
        rideRequest.setRideRequestStatus(
                RideRequestStatus.PENDING
        );

        double fare =
                fareCalculationStrategy.calculateFare(
                        rideRequestDto
                );

        rideRequest.setFare(fare);

        RideRequest savedRideRequest =
                rideRequestRepository.save(rideRequest);

        RideRequestDto response =
                modelMapper.map(
                        savedRideRequest,
                        RideRequestDto.class
                );

        response.setPickupLocation(
                GeometryUtils.toLocationDto(
                        savedRideRequest.getPickupLocation()
                )
        );

        response.setDropLocation(
                GeometryUtils.toLocationDto(
                        savedRideRequest.getDropOffLocation()
                )
        );

        response.setFare(
                savedRideRequest.getFare()
        );

        Ride assignedRide =
                rideService.matchWithDrivers(response);

        if (assignedRide != null) {
            response.setRideRequestStatus(
                    RideRequestStatus.CONFIRMED
            );

            response.setRideId(
                    assignedRide.getId()
            );
        } else {
            response.setRideRequestStatus(
                    RideRequestStatus.PENDING
            );
        }

        return response;
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDto rateDriver(
            Long rideId,
            Integer rating
    ) {
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