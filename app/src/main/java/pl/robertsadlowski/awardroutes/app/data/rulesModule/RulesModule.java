package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.airports.Airports;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;

public interface RulesModule {

	Map<String, List<Connection>> getConnectionsByOrigin();	
	List<String> getZoneNameList();
	Set<String> getAirportsByZone(String zone);
	MileageLevels getMilesNeeded(FormChoosen formChoosen);
	String getAirportZone(String airport);
	String getCountryZone(String countryCode);
	Airports getAirports();
	ZoneFilter getZoneFilterInstance();
    String getCountryNameZone(String country);
	String getAirline(String originCity, String destCity);
	String getMessage(int size, MileageLevels mileageLevels, String zoneStart, String zoneEnd);

}
