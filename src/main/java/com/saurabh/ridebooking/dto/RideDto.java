package com.saurabh.ridebooking.dto;

import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.enums.PaymentMethod;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

public class RideDto {

    private Long id;

    private Point pickupLocation;

    private Point dropLocation;

    private LocalDateTime createdTime;

    private RiderDto rider;

    private DriverDto driver;

    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;

    private Double fare;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

}
