package pl.robertsadlowski.awardroutes.view.map;

public class GeoCoordinates {
    private double lon;
    private double lat;

    public GeoCoordinates(double lon,double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public static double lonNormalizeTo180(double lon) {
        return (lon+540)%360-180;
    }

    public static double lonNormalizeTo360(double lon) {
        return ((lon%360)+360)%360;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

}
