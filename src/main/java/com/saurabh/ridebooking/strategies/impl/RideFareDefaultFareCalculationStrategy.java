package com.saurabh.ridebooking.strategies.impl;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.services.DistanceService;
import com.saurabh.ridebooking.strategies.RideFareCalculationStrategy;
import com.saurabh.ridebooking.utils.GeometryUtils;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class RideFareDefaultFareCalculationStrategy
        implements RideFareCalculationStrategy {

    private static final double BASE_FARE = 50.0;
    private static final double PER_KM_RATE = 15.0;

    private final DistanceService distanceService;
    private final GeometryFactory geometryFactory;

    public RideFareDefaultFareCalculationStrategy(
            DistanceService distanceService, GeometryFactory geometryFactory
    ) {
        this.distanceService = distanceService;
        this.geometryFactory = geometryFactory;
    }

    @Override
    public double calculateFare(RideRequestDto rideRequestDto) {

        double distance = distanceService.calculateDistance(
                GeometryUtils.toPoint(
                        rideRequestDto.getPickupLocation(),
                        geometryFactory
                ),
                GeometryUtils.toPoint(
                        rideRequestDto.getDropLocation(),
                        geometryFactory
                )
        );

        return BASE_FARE + (distance * PER_KM_RATE);
    }
}