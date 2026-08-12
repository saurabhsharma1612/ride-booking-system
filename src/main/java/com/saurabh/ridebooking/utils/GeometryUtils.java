package com.saurabh.ridebooking.utils;

import com.saurabh.ridebooking.dto.LocationDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public final class GeometryUtils {

    private static final int SRID = 4326;

    private GeometryUtils() {
    }

    public static Point toPoint(
            LocationDto location,
            GeometryFactory geometryFactory
    ) {
        Point point = geometryFactory.createPoint(
                new Coordinate(
                        location.getLongitude(),
                        location.getLatitude()
                )
        );

        point.setSRID(SRID);
        return point;
    }

    public static LocationDto toLocationDto(Point point) {
        return new LocationDto(
                point.getY(),
                point.getX()
        );
    }
}