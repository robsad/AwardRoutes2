package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.Airports;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;

public class RulesMilesMore implements IRulesModule {

	static final String ALL = "All";
	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, String> zoneByCountry = new HashMap<>();
	private Map<String, String> zoneByCountryName = new HashMap<>();
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private Map<String, List<Integer>> milesTable;
	private Map<String,String> airlines = new HashMap<>();
	private Airports airports;

	public RulesMilesMore(Map<String, List<Connection>> connectionsByOrigin,
						  Airports airports,
						  List<String> zoneNameList,
						  Map<String, List<String>> countriesByZone,
						  Map<String, List<Integer>> milesTable,
						  Map<String,String> airlines
	) {
		this.connectionsByOrigin = connectionsByOrigin;
		this.airports = airports;
		this.countriesByZone = countriesByZone;
		this.milesTable = milesTable;
		this.zoneNameList = zoneNameList;
		this.airlines = airlines;
		makeZoneByCountryMap(countriesByZone);
	}

	public Map<String, List<Connection>> getConnectionsByOrigin() {
		return connectionsByOrigin;
	}

	public List<String> getZoneNameList() {
		return zoneNameList;
	}

	public Airports getAirports() {
		return airports;
	}

	public int getMilesNeeded(String originZone, String destZone) {
		System.out.println(originZone + " " + destZone);
		if (originZone.equals(ALL)|| destZone.equals(ALL)) return 0;
		int zoneIndex = zoneNameList.indexOf(destZone);
		List<Integer> milesNeededList = milesTable.get(originZone);
		int mileageNeeded = milesNeededList.get(zoneIndex)*500;
		return mileageNeeded;
	}

	public String getAirportZone(String airport) {
		String countryCode = airports.getAirportsCountryCode(airport);
		return zoneByCountry.get(countryCode);
	}

	public String getCountryZone(String countryCode) {
		return zoneByCountry.get(countryCode);
	}

	public String getCountryNameZone(String countryName) {
		return zoneByCountryName.get(countryName);
	}

	public IZoneFilter getZoneFilterInstance() {
		return new MMZoneFilter(this);
	}

	public String getAirline(String originCity, String destCity) {
		String airline="";
		System.out.println(originCity + " -> " + destCity);
		String originCode = airports.getAirportCodeByName(originCity);
		String destCode = airports.getAirportCodeByName(destCity);
		List<Connection> connections = connectionsByOrigin.get(originCode);
		for(Connection connection : connections) {
			if (connection.getDestination().equals(destCode)) {
				airline = airlines.get(connection.getAirlinecode());
			}
		}
		return airline;
	}

	public Set<String> getAirportsByZone(String zone) {
		Set<String> airportsSet = new TreeSet<>();
		List<String> countries = countriesByZone.get(zone);
		for (String country : countries) {
			System.out.println(country);
			System.out.println(airports.getCountryByCode(country));
			System.out.println(airports.getAirportsByCountry(airports.getCountryByCode(country)));
			airportsSet.addAll(airports.getAirportsByCountry(airports.getCountryByCode(country)));
		}
		System.out.println(airportsSet);
		return airportsSet;
	}

	private void makeZoneByCountryMap(Map<String, List<String>> countriesByZone) {
		for (String key : countriesByZone.keySet()) {
			List<String> countries = countriesByZone.get(key);
			for (String country : countries) {
				zoneByCountry.put(country, key);
				zoneByCountryName.put(airports.getCountryByCode(country), key);
			}
		}
	}
}


