package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.entities.Ride;
import com.saurabh.ridebooking.entities.enums.RideStatus;
import com.saurabh.ridebooking.repository.DriverRepository;
import com.saurabh.ridebooking.repository.RideRepository;
import com.saurabh.ridebooking.repository.RideRequestRepository;
import com.saurabh.ridebooking.strategies.DriverMatchingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class RideServiceImplTest {

    private RideRepository rideRepository;
    private RideServiceImpl rideService;

    @BeforeEach
    void setUp() {
        rideRepository = mock(RideRepository.class);
        rideService = new RideServiceImpl(
                rideRepository,
                mock(RideRequestRepository.class),
                mock(DriverRepository.class),
                mock(DriverMatchingStrategy.class)
        );
    }

    @Test
    void startsAConfirmedRideAndRecordsTheStartTime() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CONFIRMED);
        when(rideRepository.save(ride)).thenReturn(ride);

        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        assertEquals(RideStatus.ONGOING, updatedRide.getRideStatus());
        assertNotNull(updatedRide.getStartedAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void rejectsAnInvalidRideStateTransitionWithoutPersistingIt() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ONGOING);

        assertThrows(
                IllegalStateException.class,
                () -> rideService.updateRideStatus(ride, RideStatus.CANCELLED)
        );

        assertEquals(RideStatus.ONGOING, ride.getRideStatus());
        verifyNoInteractions(rideRepository);
    }
}
