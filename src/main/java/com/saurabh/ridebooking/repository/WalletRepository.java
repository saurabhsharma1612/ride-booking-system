package com.saurabh.ridebooking.repository;

import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}