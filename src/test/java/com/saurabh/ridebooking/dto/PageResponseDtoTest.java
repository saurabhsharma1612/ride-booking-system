package com.saurabh.ridebooking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageResponseDtoTest {

    @Test
    void exposesExpectedPaginationMetadata() {
        PageResponseDto<String> response = PageResponseDto.from(
                new PageImpl<>(
                        List.of("ride-1", "ride-2", "ride-3"),
                        PageRequest.of(2, 5),
                        13
                )
        );

        assertEquals(
                List.of("ride-1", "ride-2", "ride-3"),
                response.content()
        );
        assertEquals(2, response.page());
        assertEquals(5, response.size());
        assertEquals(13, response.totalElements());
        assertEquals(3, response.totalPages());
    }
}
