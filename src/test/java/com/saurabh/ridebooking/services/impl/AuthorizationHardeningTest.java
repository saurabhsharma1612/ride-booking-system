package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.entities.Driver;
import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.Rider;
import com.saurabh.ridebooking.entities.User;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.exceptions.ForbiddenException;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.repository.RatingRepository;
import com.saurabh.ridebooking.repository.RideRepository;
import com.saurabh.ridebooking.repository.RiderRepository;
import com.saurabh.ridebooking.repository.UserRepository;
import com.saurabh.ridebooking.services.RatingService;
import com.saurabh.ridebooking.services.RideService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AuthorizationHardeningTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void ratingIsRejectedWhenTheAuthenticatedRiderDoesNotOwnTheRide() {
        RideRepository rideRepository = mock(RideRepository.class);
        RatingServiceImpl ratingService = new RatingServiceImpl(
                mock(RatingRepository.class),
                rideRepository,
                mock(DriverRepository.class),
                mock(RiderRepository.class)
        );

        User owner = user(10L, "owner@example.com");
        User otherUser = user(20L, "other@example.com");
        Rider ownerRider = new Rider();
        ownerRider.setUser(owner);
        Driver driver = new Driver();
        driver.setUser(user(30L, "driver@example.com"));
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ENDED);
        ride.setRider(ownerRider);
        ride.setDriver(driver);
        when(rideRepository.findById(99L)).thenReturn(Optional.of(ride));

        assertThrows(
                ForbiddenException.class,
                () -> ratingService.rateDriver(99L, otherUser, 5)
        );
    }

    @Test
    void endingAnotherDriversRideIsForbiddenBeforeChangingItsState() {
        DriverRepository driverRepository = mock(DriverRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        RideService rideService = mock(RideService.class);
        DriverServiceImpl driverService = new DriverServiceImpl(
                driverRepository,
                userRepository,
                new ModelMapper(),
                rideService,
                mock(RatingService.class)
        );

        User authenticatedUser = user(10L, "driver@example.com");
        Driver authenticatedDriver = new Driver();
        authenticatedDriver.setId(1L);
        authenticatedDriver.setUser(authenticatedUser);
        Driver assignedDriver = new Driver();
        assignedDriver.setId(2L);
        Ride ride = new Ride();
        ride.setDriver(assignedDriver);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("driver@example.com", null)
        );
        when(userRepository.findByEmail("driver@example.com"))
                .thenReturn(Optional.of(authenticatedUser));
        when(driverRepository.findByUser(authenticatedUser))
                .thenReturn(Optional.of(authenticatedDriver));
        when(rideService.getRideById(42L)).thenReturn(ride);

        assertThrows(ForbiddenException.class, () -> driverService.endRide(42L));

        verify(rideService).getRideById(42L);
        verifyNoMoreInteractions(rideService);
    }

    private User user(long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        return user;
    }
}
