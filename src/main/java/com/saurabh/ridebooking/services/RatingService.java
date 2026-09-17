package com.saurabh.ridebooking.services;

import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.User;

public interface RatingService {

    Driver rateDriver(
            Long rideId,
            User riderUser,
            Integer rating
    );

    Rider rateRider(
            Long rideId,
            User driverUser,
            Integer rating
    );
}