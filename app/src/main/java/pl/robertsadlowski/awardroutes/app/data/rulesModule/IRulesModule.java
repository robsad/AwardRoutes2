package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.Airports;

public interface IRulesModule {

	Map<String, List<Connection>> getConnectionsByOrigin();	
	List<String> getZoneNameList();
	List<String> getCountryNamesByZone(String zone);
	int getMilesNeeded(String originZone, String destZone);
	String getAirportZone(String airport);
	String getCountryZone(String countryCode);
	Airports getAirports();
	IZoneFilter getZoneFilterInstance();
    String getCountryNameZone(String country);
}
