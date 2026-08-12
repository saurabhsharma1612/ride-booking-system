package com.saurabh.ridebooking.repository;

import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Rating;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRider(Rider rider);

    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);
}