package com.saurabh.ridebooking.dto;

import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.enums.PaymentMethod;
import com.saurabh.ridebooking.entities.enums.RideRequestStatus;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {

    private Long id;

    private Point pickupLocation;

    private Point dropLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;


}
