package com.saurabh.ridebooking.controllers;

import com.saurabh.ridebooking.dto.RideRequestDto;
import com.saurabh.ridebooking.services.RiderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping("/ride/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RideRequestDto> requestRide(
            @RequestBody RideRequestDto rideRequestDto
    ) {

        RideRequestDto response =
                riderService.requestRide(rideRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}