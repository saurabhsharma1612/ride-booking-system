package com.saurabh.ridebooking.repository;

import com.saurabh.ridebooking.entities.Payment;
import com.saurabh.ridebooking.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRide(Ride ride);
}