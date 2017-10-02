package pl.robertsadlowski.awardroutes.app.data.airports;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;

public abstract class AbstractAirports {

	protected Map<String, String> airportNameByCode = new HashMap<>();
	protected Map<String, AirportsData> airportByName = new HashMap<>();
	protected Set<String> airportNames = new TreeSet<>();
	private Map<String, List<AirportsData>> airportsByCountry = new HashMap<>();
	private Map<String, Set<String>> namesByCountry = new HashMap<>();
	private Map<String, String> countryByCode = new HashMap<>();

	public AbstractAirports(Map<String, String> countryByCode){
		this.countryByCode = countryByCode;
	}

	public abstract void setTranslatedAirports(List<AirportsData> airportsData);

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
		String countryName = countryByCode.get(getAirportsCountryCode(airport));
		if (countryName==null) System.out.println("ALARM, brak kraju dla:" + airport);
		return countryName;
	}

	public String getCountryByCode(String countryCode) {
		return countryByCode.get(countryCode);
	}

	public AirportsData getAirportByName(String airportName) {
		return airportByName.get(airportName);
	}

	protected void makeCountryMap(AirportsData airport) {
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

}

