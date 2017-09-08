package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.Airports;

public class RulesMilesMore implements IRulesModule {

	static final String ALL = "All";
	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, String> zoneByCountry = new HashMap<>();
	private Map<String, String> zoneByCountryName = new HashMap<>();
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private Map<String, List<Integer>> milesTable;
	private Airports airports;

	public RulesMilesMore(Map<String, List<Connection>> connectionsByOrigin,
						  Airports airports,
						  List<String> zoneNameList,
						  Map<String, List<String>> countriesByZone,
						  Map<String, List<Integer>> milesTable
	) {
		this.connectionsByOrigin = connectionsByOrigin;
		this.airports = airports;
		this.countriesByZone = countriesByZone;
		this.milesTable = milesTable;
		this.zoneNameList = zoneNameList;
		makeZoneByCountryMap(countriesByZone);
	}

	public Map<String, List<Connection>> getConnectionsByOrigin() {
		return connectionsByOrigin;
	}

	public List<String> getZoneNameList() {
		return zoneNameList;
	}

	public List<String> getCountryNamesByZone(String zone) {
		return countriesByZone.get(zone);
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


