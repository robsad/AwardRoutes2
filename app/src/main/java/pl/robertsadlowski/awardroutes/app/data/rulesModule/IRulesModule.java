package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.Airports;

public interface IRulesModule {

	Map<String, List<Connection>> getConnectionsByOrigin();	
	List<String> getZoneNameList();
	Set<String> getAirportsByZone(String zone);
	int getMilesNeeded(String originZone, String destZone);
	String getAirportZone(String airport);
	String getCountryZone(String countryCode);
	Airports getAirports();
	IZoneFilter getZoneFilterInstance();
    String getCountryNameZone(String country);
	String getAirline(String originCity, String destCity);
	String getMessage(int size, int mileageNeeded, String zoneStart, String zoneEnd);

}
