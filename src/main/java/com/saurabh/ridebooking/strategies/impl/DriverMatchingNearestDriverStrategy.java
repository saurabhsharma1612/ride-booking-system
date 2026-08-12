package com.saurabh.ridebooking.strategies.impl;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.strategies.DriverMatchingStrategy;
import com.saurabh.ridebooking.utils.GeometryUtils;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class DriverMatchingNearestDriverStrategy
        implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;
    private final GeometryFactory geometryFactory;

    public DriverMatchingNearestDriverStrategy(
            DriverRepository driverRepository, GeometryFactory geometryFactory
    ) {
        this.driverRepository = driverRepository;
        this.geometryFactory = geometryFactory;
    }



    @Override
    public List<Driver> findMatchingDriver(
            RideRequestDto rideRequestDto
    ) {

        return driverRepository.findTenNearestDrivers(
                GeometryUtils.toPoint(
                        rideRequestDto.getPickupLocation(),
                        geometryFactory
                )
        );
    }
}