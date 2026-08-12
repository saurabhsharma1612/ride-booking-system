package com.saurabh.ridebooking.services.impl;

import com.saurabh.ridebooking.services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public double calculateDistance(Point src, Point dest) {

        double lat1 = Math.toRadians(src.getY());
        double lon1 = Math.toRadians(src.getX());

        double lat2 = Math.toRadians(dest.getY());
        double lon2 = Math.toRadians(dest.getX());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a =
                Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                        + Math.cos(lat1)
                        * Math.cos(lat2)
                        * Math.sin(deltaLon / 2)
                        * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return EARTH_RADIUS_KM * c;
    }
}