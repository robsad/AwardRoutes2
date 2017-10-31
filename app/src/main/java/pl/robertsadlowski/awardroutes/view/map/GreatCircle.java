package pl.robertsadlowski.awardroutes.view.map;

import java.util.ArrayList;
import java.util.List;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public class GreatCircle {

    public List<GeoCoordinates> getGreatCirclePath(AirportsData startAirportData, AirportsData endAirportData) {
        GeoCoordinates[] geoPoint = new GeoCoordinates[9];
        double lon = Math.toRadians(GeoCoordinates.lonNormalizeTo360(startAirportData.getLon()));
        double lat = Math.toRadians(startAirportData.getLat());
        geoPoint[0] = new GeoCoordinates(lon,lat);
        lon = Math.toRadians(GeoCoordinates.lonNormalizeTo360(endAirportData.getLon()));
        lat = Math.toRadians(endAirportData.getLat());
        geoPoint[8] = new GeoCoordinates(lon,lat);
        geoPoint[4] = getHalfWay(geoPoint[0],geoPoint[8]);
        geoPoint[2] = getHalfWay(geoPoint[0],geoPoint[4]);
        geoPoint[6] = getHalfWay(geoPoint[4],geoPoint[8]);
        geoPoint[1] = getHalfWay(geoPoint[0],geoPoint[2]);
        geoPoint[3] = getHalfWay(geoPoint[2],geoPoint[4]);
        geoPoint[5] = getHalfWay(geoPoint[4],geoPoint[6]);
        geoPoint[7] = getHalfWay(geoPoint[6],geoPoint[8]);
        List<GeoCoordinates> path = new ArrayList<GeoCoordinates>();
        for (int i=0;i<9;i++) {
            double latNormalized = Math.toDegrees(geoPoint[i].getLat());
            double lonNormalized = GeoCoordinates.lonNormalizeTo180(Math.toDegrees(geoPoint[i].getLon()));
            path.add(new GeoCoordinates(lonNormalized,latNormalized));
        }
        return path;
    }

    private GeoCoordinates getHalfWay(GeoCoordinates geoPoint1, GeoCoordinates geoPoint2) {
        double lon1 = geoPoint1.getLon();
        double lat1 = geoPoint1.getLat();
        double lon2 = geoPoint2.getLon();
        double lat2 = geoPoint2.getLat();
        double bx = Math.cos(lat2) * Math.cos(lon2-lon1);
        double by = Math.cos(lat2) * Math.sin(lon2-lon1);
        double latMid = Math.atan2(Math.sin(lat1) + Math.sin(lat2),Math.sqrt( (Math.cos(lat1)+bx)*(Math.cos(lat1)+bx) + by*by ));
        double lonMid = lon1 + Math.atan2(by, Math.cos(lat1) + bx);
        return new GeoCoordinates(lonMid,latMid);
    }



}
