package com.saurabh.ridebooking.controllers;

import com.saurabh.ridebooking.dto.DriverDto;
import com.saurabh.ridebooking.dto.PageResponseDto;
import com.saurabh.ridebooking.dto.RideDto;
import com.saurabh.ridebooking.dto.RiderDto;
import com.saurabh.ridebooking.services.DriverService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/driver")
@PreAuthorize("hasRole('DRIVER')")
@Validated
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
    public ResponseEntity<PageResponseDto<RideDto>> getAllMyRides(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be zero or greater") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must not exceed 100") int size
    ) {

        return ResponseEntity.ok(
                driverService.getAllMyRides(page, size)
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

    @PutMapping("/rides/{rideId}/rate-rider")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RiderDto> rateRider(
            @PathVariable Long rideId,
            @RequestParam Integer rating
    ) {

        return ResponseEntity.ok(
                driverService.rateRider(
                        rideId,
                        rating
                )
        );
    }

}
