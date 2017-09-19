package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.logic.Container;

public class MMZoneFilter implements IZoneFilter {

	private int size;
	private FormChoosen formChoosen;
	private IRulesModule rulesModule;
	private String startZone;
	private String endZone;
	private List<Set<String>> zoneCalculation = new ArrayList<Set<String>>(Collections.<Set<String>>emptyList());
	
	public MMZoneFilter (IRulesModule rulesModule) {
		this.rulesModule = rulesModule;
	}

	public List<Set<String>> calculateZones(FormChoosen formChoosen) {
		this.size = formChoosen.getSize();
		this.startZone = formChoosen.getStartZone();
		this.endZone = formChoosen.getEndZone();
		this.formChoosen = formChoosen;
		zoneScan();
		return zoneCalculation;
	}

	public String getStartZone() {
		return startZone;
	}

	public String getEndZone() {
		return endZone;
	}

	private void zoneScan() {
		int endOfStartZone = 0;
		int startOfEndZone = 0;
		int numberOfZones = 0;
		String zoneLast = Container.ANY_ZONE;
		String zoneStart = Container.ANY_ZONE;
		String zoneEnd = Container.ANY_ZONE;
		for (int i = 0; i < size; i++) {
			String zoneNow = whatZone(i);
			if ((!zoneNow.equals(Container.ANY_ZONE)) && (!zoneNow.equals(zoneLast))) {
				zoneLast = zoneNow;
				numberOfZones++;
				if (numberOfZones == 1) {
					endOfStartZone = i + 1;
					zoneStart = zoneNow;
				}
				if (numberOfZones == 2) {
					startOfEndZone = i;
					zoneEnd = zoneNow;
				}
			}
		}
		if (numberOfZones > 1) {
			makeZoneMap(endOfStartZone, zoneStart, startOfEndZone, zoneEnd);
		}
		else if ((numberOfZones == 1)&&(size==3)) {
			mapWithOneZone(zoneStart);
		} else {
			mapTempOneZone();
		}
	}

	private String whatZone(int i) {
		if ((i == 0) && (!startZone.equals(Container.ANY_ZONE)))
			return startZone;
		if ((i == size-1) && (!endZone.equals(Container.ANY_ZONE)))
			return endZone;
		String countryName = formChoosen.getCountry(i);
		if (!countryName.equals(Container.ANY_COUNTRY)) {
			return rulesModule.getCountryNameZone(countryName);
		}
		String airportName = formChoosen.getAirport(i);
		if (!airportName.equals(Container.ANY_AIRPORT)) {
			return rulesModule.getAirportZone(airportName);
		}
		return Container.ANY_ZONE;
	}

	private void makeZoneMap(int start, String startZone, int end,
							 String endZone) {
		Set<String> zonesA = new TreeSet<>();
		Set<String> zonesB = new TreeSet<>();
		Set<String> zonesC = new TreeSet<>();
		zonesA.add(startZone);
		zonesB.add(startZone);
		zonesB.add(endZone);
		zonesC.add(endZone);
		addCalculation(0, start, zonesA);
		addCalculation(start, end, zonesB);
		addCalculation(end, size, zonesC);
		this.startZone = startZone;
		this.endZone = endZone;
	}

	public void addCalculation(int start, int end, Set<String> zone) {
		for (int i = start; i < end; i++) {
			zoneCalculation.add(zone);
		}
	}

	private void mapWithOneZone(String zone) {
		for (int i = 0; i < size; i++) {
			Set<String> zones = new TreeSet<>();
			zones.add(zone);
			zoneCalculation.add(zones);
		}
		this.startZone = zone;
		this.endZone = zone;
	}

	private void mapTempOneZone() {
		for (int i = 0; i < size; i++) {
			String zone = whatZone(i);
			Set<String> zones = new TreeSet<>();
			if (zone.equals(Container.ANY_ZONE)) {
				zones.add(Container.ANY_ZONE);
			} else {
				zones.add(zone);
				if (i==0) this.startZone = zone;
				if (i==size-1) this.endZone = zone;
			}
			zoneCalculation.add(zones);
		}
	}

}
