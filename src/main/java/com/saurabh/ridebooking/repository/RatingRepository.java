package com.saurabh.ridebooking.repository;

import com.saurabh.ridebooking.entities.Rating;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRideAndRatedBy(
            Ride ride,
            User ratedBy
    );

    List<Rating> findByRatedUser(
            User ratedUser
    );
}