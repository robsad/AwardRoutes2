package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.airports.AbstractAirports;
import pl.robertsadlowski.awardroutes.app.data.airports.AirportsMilesMore;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.logic.Container;

public class RulesMilesMore implements IRulesModule {

	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, String> zoneByCountry = new HashMap<>();
	private Map<String, String> zoneByCountryName = new HashMap<>();
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private Map<String, List<Integer>> milesTable;
	private Map<String,String> airlines = new HashMap<>();
	private AirportsMilesMore airports;

	public RulesMilesMore(Map<String, List<Connection>> connectionsByOrigin,
						  List<AirportsData> airportsData,
						  Map<String, String> countryByCode,
						  List<String> zoneNameList,
						  Map<String, List<String>> countriesByZone,
						  Map<String, List<Integer>> milesTable,
						  Map<String,String> airlines
	) {
		this.connectionsByOrigin = connectionsByOrigin;
		this.countriesByZone = countriesByZone;
		this.milesTable = milesTable;
		this.zoneNameList = zoneNameList;
		this.airlines = airlines;
		airports = new AirportsMilesMore(countryByCode);
		airports.setTranslatedAirports(airportsData);
		makeZoneByCountryMap(countriesByZone);
	}

	public Map<String, List<Connection>> getConnectionsByOrigin() {
		return connectionsByOrigin;
	}

	public List<String> getZoneNameList() {
		return zoneNameList;
	}

	public AbstractAirports getAirports() {
		return airports;
	}

	public int getMilesNeeded(int size, String originZone, String destZone) {
		System.out.println(originZone + " " + destZone);
		if (originZone.equals(Container.ANY_ZONE)|| destZone.equals(Container.ANY_ZONE)) return 0;
		int zoneIndex = zoneNameList.indexOf(destZone);
		List<Integer> milesNeededList = milesTable.get(originZone);
		int mileageNeeded = milesNeededList.get(zoneIndex)*500;
		if (size==5)  {
			mileageNeeded=50000;
		}
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
		if (zoneByCountryName.get(countryName)==null) System.out.println("ALARM: "+countryName);
		return zoneByCountryName.get(countryName);
	}

	public IZoneFilter getZoneFilterInstance() {
		return new MMZoneFilter(this);
	}

	public String getMessage(int size, int mileageNeeded, String zoneStart, String zoneEnd) {
		String message = null;
		if (((size>3)&&(zoneStart.equals(zoneEnd)))&&(!zoneStart.equals(Container.ANY_ZONE))) {
			message = "Travel within zone possible with up to 3 segments only";
		}
		if ((size<5)&&(mileageNeeded==50000)) {
			message = "Travel possible with 5 segments without zone rules";
		}
		return message;
	}

	public String getAirline(String originCity, String destCity) {
		String airline="";
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
			airportsSet.addAll(airports.getAirportsByCountry(airports.getCountryByCode(country)));
		}
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


