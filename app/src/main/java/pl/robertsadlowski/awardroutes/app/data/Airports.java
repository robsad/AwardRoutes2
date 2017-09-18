package pl.robertsadlowski.awardroutes.app.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public class Airports {

	private Map<String, String> airportNameByCode = new HashMap<>();
	private Map<String, AirportsData> airportByName = new HashMap<>();
	private Map<String, List<AirportsData>> airportsByCountry = new HashMap<>();
	private Map<String, Set<String>> namesByCountry = new HashMap<>();
	private Set<String> airportNames = new TreeSet<>();
	private Map<String, String> countryByCode = new HashMap<>();

	public Airports(List<AirportsData> airportsData, Map<String, String> countryByCode){
		this.countryByCode = countryByCode;
		translateAirports(airportsData);
	}

	public Set<String> getAllAirportNames() {
		return new TreeSet<String>(airportNames);
	}

	public Set<String> getAirportsByCountry(String country) {
		Set<String> airportsNames = namesByCountry.get(country);
		if (airportsNames==null) return Collections.<String>emptySet();
		return airportsNames;
	}

	public String getAirportCodeByName(String airportName) {
		return getAirportByName(airportName).getCityCode();
	}

	public String getAirportNameByCode(String airportCode) {
		return airportNameByCode.get(airportCode);
	}

	public String getAirportsCountryCode(String airport) {
		return getAirportByName(airport).getCountryCode();
	}

	public String getAirportsCountryName(String airport) {
		return countryByCode.get(getAirportsCountryCode(airport));
	}

	public String getCountryByCode(String countryCode) {
		return countryByCode.get(countryCode);
	}

	private AirportsData getAirportByName(String airportName) {
		return airportByName.get(airportName);
	}

	private void translateAirports(List<AirportsData> airportsData){
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
			makeCountryMap(airport);
		}
	}

	private void makeCountryMap(AirportsData airport) {
		List<AirportsData> airportByCountry;
		Set<String> nameByCountry;
		String countryKey = airport.getCountryCode();
		if (airportsByCountry.containsKey(countryKey)) {
			airportByCountry = airportsByCountry.get(countryKey);
			nameByCountry = namesByCountry.get(countryByCode.get(countryKey));
		} else {
			airportByCountry = new LinkedList<>();
			nameByCountry = new TreeSet<>();
		}
		airportByCountry.add(airport);
		airportsByCountry.put(countryKey, airportByCountry);
		nameByCountry.add(airport.getCityName()+","+airport.getCountryCode());
		namesByCountry.put(countryByCode.get(countryKey), nameByCountry);
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

