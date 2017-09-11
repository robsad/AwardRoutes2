package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;

public class MMZoneFilter implements IZoneFilter {

	static final String ALL = "All";
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
		String zoneLast = ALL;
		String zoneStart = ALL;
		String zoneEnd = ALL;
		for (int i = 0; i < size; i++) {
			String zoneNow = whatZone(i);
			System.out.println("ZoneNow: " +  zoneNow);
			if ((!zoneNow.equals(ALL)) && (!zoneNow.equals(zoneLast))) {
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
		} else {
			for (int i = 0; i < size; i++) {
				String zone = whatZone(i);
				Set<String> zones = new TreeSet<>();
				if (zone.equals(ALL)) {
					zones.add(ALL);
				} else {
					zones.add(zone);
				}
				zoneCalculation.add(zones);
			}
		}
	}

	private String whatZone(int i) {
		if ((i == 0) && (!startZone.equals(ALL)))
			return startZone;
		if ((i == size-1) && (!endZone.equals(ALL)))
			return endZone;
		String countryName = formChoosen.getCountry(i);
		System.out.println("i= " + i + " country= " +countryName);  //comment
		if (!countryName.equals(ALL)) {
			System.out.println("rulesModule.getCountryZone(country) = " + rulesModule.getCountryNameZone(countryName)); //comment
			return rulesModule.getCountryNameZone(countryName);
		}
		String airportName = formChoosen.getAirport(i);
		if (!airportName.equals(ALL)) {
			return rulesModule.getAirportZone(airportName);
		}
		return ALL;
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

}
