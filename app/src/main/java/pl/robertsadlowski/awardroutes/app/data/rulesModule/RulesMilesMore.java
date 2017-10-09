package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.airports.Airports;
import pl.robertsadlowski.awardroutes.app.data.airports.AirportsMilesMore;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.logic.Container;

public class RulesMilesMore implements RulesModule {

	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, String> zoneByCountry = new HashMap<>();
	private Map<String, String> zoneByCountryName = new HashMap<>();
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private Map<String, List<MileageLevels>> milesTable;
	private Map<String, MileageLevels> milesTableDomestic;
	private Map<String,String> airlines = new HashMap<>();
	private AirportsMilesMore airports;
	private ZoneFilter zoneFilter;

	public RulesMilesMore(Map<String, List<Connection>> connectionsByOrigin,
						  List<AirportsData> airportsData,
						  Map<String, String> countryByCode,
						  List<String> zoneNameList,
						  Map<String, List<String>> countriesByZone,
						  Map<String, List<MileageLevels>> milesTable,
						  Map<String, MileageLevels> milesTableDomestic,
						  Map<String,String> airlines
	) {
		this.connectionsByOrigin = connectionsByOrigin;
		this.countriesByZone = countriesByZone;
		this.milesTable = milesTable;
		this.milesTableDomestic = milesTableDomestic;
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

	public Airports getAirports() {
		return airports;
	}

	public ZoneFilter getZoneFilterInstance() {
		zoneFilter = new ZoneFilterMilesMore(this);
		return zoneFilter;
	}

	public MileageLevels getMilesNeeded(FormChoosen formChoosen) {
		String originZone = zoneFilter.getStartZone();
		String destZone = zoneFilter.getEndZone();
		int size = formChoosen.getSize();
		if (originZone.equals(Container.ANY_ZONE)|| destZone.equals(Container.ANY_ZONE)) return new MileageLevels("0","0");
		if (originZone.equals(destZone)) {
			String country = whatCountryDomestic(formChoosen);
			System.out.println("Country " + country);
			if (!country.equals(Container.ANY_COUNTRY)) {
				if (milesTableDomestic.containsKey(country)) {
					return milesTableDomestic.get(country);
				} else {
					return milesTableDomestic.get("ALL");
				}
			}
		}
		int zoneIndex = zoneNameList.indexOf(destZone);
		List<MileageLevels> milesNeededList = milesTable.get(originZone);
		MileageLevels mileageNeeded = milesNeededList.get(zoneIndex);
		if (size==5)  {
			mileageNeeded = new MileageLevels("100","185");
		}
		return mileageNeeded;
	}

	private String whatCountryDomestic(FormChoosen formChoosen) {
		int size = formChoosen.getSize();
		String countryLast = whatCountry(0,formChoosen);
		for (int i = 1; i < size; i++) {
			String countryNow = whatCountry(i,formChoosen);
			if (countryLast.equals(countryNow)) {
				countryLast = countryNow;
			} else {
				return Container.ANY_COUNTRY;
			}
		}
		return countryLast;
	}

	private String whatCountry(int i, FormChoosen formChoosen) {
		String countryName = formChoosen.getCountry(i);
		if (countryName.equals(Container.ANY_COUNTRY)) {
			String airportName = formChoosen.getAirport(i);
			if (!airportName.equals(Container.ANY_AIRPORT)) {
				countryName = airports.getAirportsCountryName(airportName);
			}
		}
		return countryName;
	}

	public String getMessage(int size, MileageLevels mileageLevels, String zoneStart, String zoneEnd) {
		String message = null;
		if (((size>3)&&(zoneStart.equals(zoneEnd)))&&(!zoneStart.equals(Container.ANY_ZONE))) {
			message = "Travel within zone possible with up to 3 segments only";
		}
		if ((size<5)&&(mileageLevels.getY()==100)) {
			message = "Travel possible with 5 segments without zone rules";
		}
		return message;
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


