package com.saurabh.ridebooking.utils;

import org.springframework.data.domain.PageRequest;

public final class PaginationUtils {

    public static final int MAX_PAGE_SIZE = 100;

    private PaginationUtils() {
    }

    public static PageRequest createPageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be zero or greater");
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and " + MAX_PAGE_SIZE
            );
        }

        return PageRequest.of(page, size);
    }
}
