package pl.robertsadlowski.awardroutes.app.data.airports;

import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public class AirportsMilesMore extends Airports {


    public AirportsMilesMore(Map<String, String> countryByCode) {
        super(countryByCode);
    }

    public void setTranslatedAirports(List<AirportsData> airportsData){
        for (AirportsData airport : airportsData) {
            if (isHawaii(airport)) {
                airport.setCountryCode("USH");
            }
            if (isCanary(airport)) {
                airport.setCountryCode("ESC");
            }
            if (isAzores(airport)) {
                airport.setCountryCode("PTA");
            }
            airportNameByCode.put(airport.getCityCode(), airport.getCityName()+","+airport.getCountryCode());
            airportByName.put(airport.getCityName()+","+airport.getCountryCode(), airport);
            airportNames.add(airport.getCityName()+","+airport.getCountryCode());
            super.makeCountryMap(airport);
        }
    }

    private boolean isHawaii(AirportsData airport) {
        double lat = airport.getLat();
        double lon = airport.getLon();
        if (isHawaiiLat(lat) && isHawaiiLon(lon))
            return true;
        else
            return false;
    }

    private boolean isCanary(AirportsData airport) {
        double lat = airport.getLat();
        double lon = airport.getLon();
        if (isCanaryLat(lat) && isCanaryLon(lon))
            return true;
        else
            return false;
    }

    private boolean isAzores(AirportsData airport) {
        double lat = airport.getLat();
        double lon = airport.getLon();
        if (isAzoresLat(lat) && isAzoresLon(lon))
            return true;
        else
            return false;
    }

    private boolean isHawaiiLat(double lat) {
        if ((lat > 18) && (lat < 23))
            return true;
        else
            return false;
    }

    private boolean isHawaiiLon(double lon) {
        if ((lon > -160) && (lon < -154))
            return true;
        else
            return false;
    }

    private boolean isCanaryLat(double lat) {
        if ((lat > 27.5) && (lat < 30))
            return true;
        else
            return false;
    }

    private boolean isCanaryLon(double lon) {
        if ((lon > -19) && (lon < -13.2))
            return true;
        else
            return false;
    }

    private boolean isAzoresLat(double lat) {
        if ((lat > 32) && (lat < 41))
            return true;
        else
            return false;
    }

    private boolean isAzoresLon(double lon) {
        if ((lon > -34) && (lon < -16))
            return true;
        else
            return false;
    }
}
