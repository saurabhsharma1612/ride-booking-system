package com.saurabh.ridebooking.repository;

import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query(value = """
        SELECT d.*
        FROM driver d
        WHERE d.available = true
          AND ST_DWithin(
              d.current_location::geography,
              ST_SetSRID(:pickUpLocation, 4326)::geography,
              10000
          )
        ORDER BY ST_Distance(
            d.current_location::geography,
            ST_SetSRID(:pickUpLocation, 4326)::geography
        )
        LIMIT 10
        """, nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickUpLocation);

    @Query(value = """
        SELECT d.*
        FROM driver d
        WHERE d.available = true
          AND ST_DWithin(
              d.current_location::geography,
              ST_SetSRID(:pickUpLocation, 4326)::geography,
              15000
          )
        ORDER BY d.rating DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDrivers(Point pickUpLocation);

    Optional<Driver> findByUser(User user);
}