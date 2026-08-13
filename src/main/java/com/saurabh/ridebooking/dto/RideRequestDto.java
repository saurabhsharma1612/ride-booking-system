package com.saurabh.ridebooking.dto;

import com.saurabh.ridebooking.entities.enums.PaymentMethod;
import com.saurabh.ridebooking.entities.enums.RideRequestStatus;
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

    private LocationDto pickupLocation;

    private LocationDto dropLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;

    private Double fare;
}
