package pl.robertsadlowski.awardroutes.view.map;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public class GreatCircle {

    public static GeoCoordinates getHalfWayLong(AirportsData startAirportData, AirportsData endAirportData) {

        double lon1 = Math.toRadians(GeoCoordinates.lonNormalizeTo360(startAirportData.getLon()));
        double lat1 = Math.toRadians(startAirportData.getLat());
        double lon2 = Math.toRadians(GeoCoordinates.lonNormalizeTo360(endAirportData.getLon()));
        double lat2 = Math.toRadians(endAirportData.getLat());
        double bx = Math.cos(lat2) * Math.cos(lon2-lon1);
        double by = Math.cos(lat2) * Math.sin(lon2-lon1);

        double latMid = Math.atan2(Math.sin(lat1) + Math.sin(lat2),Math.sqrt( (Math.cos(lat1)+bx)*(Math.cos(lat1)+bx) + by*by ));
        double lonMid = lon1 + Math.atan2(by, Math.cos(lat1) + bx);

        double latMidNormalized = Math.toDegrees(latMid);
        double lonMidNormalized = GeoCoordinates.lonNormalizeTo180(Math.toDegrees(lonMid));

        return new GeoCoordinates(lonMidNormalized,latMidNormalized);
    }

}
