package pl.robertsadlowski.awardroutes.app.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public class Airports {

	private Map<String, AirportsData> airportByCode = new HashMap<>();
	private Map<String, AirportsData> airportByName = new HashMap<>();
	private Map<String, List<AirportsData>> airportsByCountry = new HashMap<>();
	private Map<String, Set<String>> namesByCountry = new HashMap<>();
	private Set<String> airportNames = new TreeSet<>();
	private Set<String> countries = new TreeSet<>();
	private Map<String, String> countryByCode = new HashMap<>();
	
	public Airports(List<AirportsData> airportsData, Map<String, String> countryByCode){
		this.countryByCode = countryByCode;
		translateAirports(airportsData);
	}
	
	public Set<String> getAirportNames() {
		return airportNames;
	}
	
	public Set<String> getCountries() {
		return countries;
	}
	
	public Set<String> getAirportsByCountry(String country) {
		return namesByCountry.get(country);
	}
	
	public AirportsData getAirportByName(String airportName) {
		return airportByName.get(airportName);
	}
	
	public AirportsData getAirportByCode(String airportCode) {
		return airportByCode.get(airportCode);
	}
	
	public String getAirportCodeByName(String airportName) {
		return getAirportByName(airportName).getCityCode();
	}
	
	public String getAirportNameByCode(String airportCode) {
		return getAirportByCode(airportCode).getCityName();
	}
	
	public String getAirportsCountryCode(String airport) {
		return getAirportByName(airport).getCountryCode();
	}

	public String getAirportsCountryName(String airport) {
		return countryByCode.get(getAirportsCountryCode(airport));
	}

	public Set<String> getAllAirportNames() {
		return airportNames;
	}
	
	private void translateAirports(List<AirportsData> airportsData){
		for (AirportsData airport : airportsData) {
			airportByCode.put(airport.getCityCode(), airport);
			airportByName.put(airport.getCityName(), airport);
			airportNames.add(airport.getCityName());
			makeCountryMap(airport);
		}
	}
	
	private void makeCountryMap(AirportsData airport) {
			List<AirportsData> airportByCountry;
			Set<String> nameByCountry;
			String countryKey = airport.getCountryCode();
			if (isHawaii(airport)) {
				countryKey = "USH";
				airport.setCountryCode("USH");
			}
			if (airportsByCountry.containsKey(countryKey)) {
				airportByCountry = airportsByCountry.get(countryKey);
				nameByCountry = namesByCountry.get(countryKey);
			} else {
				airportByCountry = new LinkedList<>();
				nameByCountry = new TreeSet<>();
			}
				airportByCountry.add(airport);
				airportsByCountry.put(countryKey,airportByCountry);
				nameByCountry.add(airport.getCityName());
				namesByCountry.put(countryKey,nameByCountry);
		if (airport.getCityName().equals("Honolulu")) System.out.println("Hawaje" + airport.getCountryCode() + airport.getLat() + " " + airport.getLon());
			}
					
	
	private boolean isHawaii(AirportsData airport) {
		double lat = airport.getLat();
		double lon = airport.getLon();
		if (((lat>18)&&(lat<23))&&((lon>-160)&&(lon<-154))) return true;
		else
		return false;
	}
	
}

