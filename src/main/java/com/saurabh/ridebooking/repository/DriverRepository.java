package com.saurabh.ridebooking.repository;

import com.saurabh.ridebooking.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.saurabh.ridebooking.entities.User;
import org.locationtech.jts.geom.Point;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query(value = """
        SELECT d.*, ST_Distance(d.current_location, :pickUpLocation) AS distance
        FROM driver d
        WHERE d.available = true
          AND ST_DWithin(d.current_location, :pickUpLocation, 10000)
        ORDER BY distance
        LIMIT 10
        """, nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickUpLocation);

    @Query(value = """
        SELECT d.*
        FROM driver d
        WHERE d.available = true
          AND ST_DWithin(d.current_location, :pickUpLocation, 15000)
        ORDER BY d.rating DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDrivers(Point pickUpLocation);

    Optional<Driver> findByUser(User user);

}
