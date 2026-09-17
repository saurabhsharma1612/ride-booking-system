package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Rating;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.exceptions.ForbiddenException;
import com.saurabh.ridebooking.exceptions.ResourceNotFoundException;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.repository.RatingRepository;
import com.saurabh.ridebooking.repository.RideRepository;
import com.saurabh.ridebooking.repository.RiderRepository;
import com.saurabh.ridebooking.services.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;

    public RatingServiceImpl(
            RatingRepository ratingRepository,
            RideRepository rideRepository,
            DriverRepository driverRepository,
            RiderRepository riderRepository
    ) {
        this.ratingRepository = ratingRepository;
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.riderRepository = riderRepository;
    }

    @Override
    @Transactional
    public Driver rateDriver(
            Long rideId,
            User riderUser,
            Integer ratingValue
    ) {

        validateRating(ratingValue);

        Ride ride = getRide(rideId);

        validateRideCanBeRated(ride);

        if (ride.getRider() == null
                || ride.getRider().getUser().getId() != riderUser.getId()) {

            throw new ForbiddenException(
                    "Ride is not assigned to this rider"
            );
        }

        Driver driver = ride.getDriver();

        if (driver == null) {
            throw new IllegalStateException(
                    "Ride does not have an assigned driver"
            );
        }

        createRating(
                ride,
                riderUser,
                driver.getUser(),
                ratingValue
        );

        updateDriverRating(driver);

        return driver;
    }

    @Override
    @Transactional
    public Rider rateRider(
            Long rideId,
            User driverUser,
            Integer ratingValue
    ) {

        validateRating(ratingValue);

        Ride ride = getRide(rideId);

        validateRideCanBeRated(ride);

        if (ride.getDriver() == null
                || ride.getDriver().getUser().getId() != driverUser.getId()) {

            throw new ForbiddenException(
                    "Ride is not assigned to this driver"
            );
        }

        Rider rider = ride.getRider();

        if (rider == null) {
            throw new IllegalStateException(
                    "Ride does not have an assigned rider"
            );
        }

        createRating(
                ride,
                driverUser,
                rider.getUser(),
                ratingValue
        );

        updateRiderRating(rider);

        return rider;
    }

    private Ride getRide(Long rideId) {

        return rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ride not found"
                        )
                );
    }

    private void validateRideCanBeRated(Ride ride) {

        if (ride.getRideStatus() != RideStatus.ENDED) {

            throw new IllegalStateException(
                    "Only an ended ride can be rated"
            );
        }
    }

    private void validateRating(Integer rating) {

        if (rating == null || rating < 1 || rating > 5) {

            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5"
            );
        }
    }

    private void createRating(
            Ride ride,
            User ratedBy,
            User ratedUser,
            Integer ratingValue
    ) {

        if (ratingRepository
                .findByRideAndRatedBy(ride, ratedBy)
                .isPresent()) {

            throw new IllegalStateException(
                    "You have already rated this ride"
            );
        }

        Rating rating = new Rating();

        rating.setRide(ride);
        rating.setRatedBy(ratedBy);
        rating.setRatedUser(ratedUser);
        rating.setRating(ratingValue);

        ratingRepository.save(rating);
    }

    private void updateDriverRating(Driver driver) {

        List<Rating> ratings =
                ratingRepository.findByRatedUser(
                        driver.getUser()
                );

        double average =
                ratings.stream()
                        .mapToInt(Rating::getRating)
                        .average()
                        .orElse(0.0);

        driver.setRating(average);

        driverRepository.save(driver);
    }

    private void updateRiderRating(Rider rider) {

        List<Rating> ratings =
                ratingRepository.findByRatedUser(
                        rider.getUser()
                );

        double average =
                ratings.stream()
                        .mapToInt(Rating::getRating)
                        .average()
                        .orElse(0.0);

        rider.setRating(average);

        riderRepository.save(rider);
    }
}