package pl.robertsadlowski.awardroutes.app.data.airports;

import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public class AirportsAAdvantage extends AbstractAirports {

    public AirportsAAdvantage(Map<String, String> countryByCode) {
        super(countryByCode);
    }

    public void setTranslatedAirports(List<AirportsData> airportsData){
        for (AirportsData airport : airportsData) {
            if (isHawaii(airport)) {
                airport.setCountryCode("USH");
            }
            if (isManaus(airport)) {
                airport.setCountryCode("BRM");
            }
            if (isEaster(airport)) {
                airport.setCountryCode("CLE");
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

    private boolean isManaus(AirportsData airport) {
        if (airport.getCityName().equals("Manaus"))
            return true;
        else
            return false;
    }

    private boolean isEaster(AirportsData airport) {
        if (airport.getCityName().equals("Easter Island"))
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

}
