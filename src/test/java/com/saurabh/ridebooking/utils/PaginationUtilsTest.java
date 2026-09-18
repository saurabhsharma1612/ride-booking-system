package com.saurabh.ridebooking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaginationUtilsTest {

    @Test
    void createsAPageRequestWithinTheSupportedBounds() {
        PageRequest pageRequest = PaginationUtils.createPageRequest(2, 25);

        assertEquals(2, pageRequest.getPageNumber());
        assertEquals(25, pageRequest.getPageSize());
    }

    @Test
    void rejectsANegativePage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PaginationUtils.createPageRequest(-1, 10)
        );
    }

    @Test
    void rejectsPageSizesOutsideTheSupportedBounds() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PaginationUtils.createPageRequest(0, 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> PaginationUtils.createPageRequest(0, 101)
        );
    }
}
