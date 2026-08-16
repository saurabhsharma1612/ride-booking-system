package com.saurabh.ridebooking.controllers;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.services.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
@PreAuthorize("hasRole('DRIVER')")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/profile")
    public ResponseEntity<DriverDto> getMyProfile() {

        return ResponseEntity.ok(
                driverService.getMyProfile()
        );
    }

    @GetMapping("/rides")
    public ResponseEntity<List<RideDto>> getAllMyRides() {

        return ResponseEntity.ok(
                driverService.getAllMyRides()
        );
    }

    @PutMapping("/rides/{rideId}/start")
    public ResponseEntity<RideDto> startRide(
            @PathVariable Long rideId
    ) {

        return ResponseEntity.ok(
                driverService.startRide(rideId)
        );
    }

    @PutMapping("/rides/{rideId}/end")
    public ResponseEntity<RideDto> endRide(
            @PathVariable Long rideId
    ) {

        return ResponseEntity.ok(
                driverService.endRide(rideId)
        );
    }
}