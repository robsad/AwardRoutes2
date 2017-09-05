package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.Airports;

public class RulesMilesMore implements IRulesModule {

	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, String> zoneOfCountry = new HashMap<>();
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
		makeZoneOfCountryMap(countriesByZone); 
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
		int zoneIndex = zoneNameList.indexOf(destZone);
		List<Integer> milesNeededList = milesTable.get(originZone);
		int milageNeeded = milesNeededList.get(zoneIndex);
		return milageNeeded;
	}
	
	public String getAirportZone(String airport) {
		String countryCode = airports.getAirportsCountryCode(airport);
		//System.out.println("airport: "+airport+" countryCode: "+countryCode);
		return zoneOfCountry.get(countryCode);
	}
	
	public String getCountryZone(String countryCode) {
		return zoneOfCountry.get(countryCode);
	}

	public IZoneFilter getZoneFilterInstance() {
		return new MMZoneFilter(this);
	}
	
	private void makeZoneOfCountryMap(Map<String, List<String>> countriesByZone) {	
		for (String key : countriesByZone.keySet()) {
			List<String> countries = countriesByZone.get(key);
			for (String country : countries) {
				zoneOfCountry.put(country, key);
			}
		}
	}
	
}


