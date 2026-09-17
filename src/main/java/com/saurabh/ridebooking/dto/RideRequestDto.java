package com.saurabh.ridebooking.dto;

import com.saurabh.ridebooking.entities.enums.PaymentMethod;
import com.saurabh.ridebooking.entities.enums.RideRequestStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {

    private Long id;

    private Long rideId;

    @Valid
    @NotNull(message = "Pickup location is required")
    private LocationDto pickupLocation;

    @Valid
    @NotNull(message = "Drop location is required")
    private LocationDto dropLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;

    private Double fare;
}
