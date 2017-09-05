package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;

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
		this.endZone = formChoosen.getStartZone();
		this.formChoosen = formChoosen;
		zoneScan();
		return zoneCalculation;
	}
		
	private void zoneScan() {
		int endOfStartZone = 0;
		int startOfEndZone = 0;
		int numberOfZones = 0;
		String zoneLast = "All";
		String zoneStart = "All";
		String zoneEnd = "All";
		for(int i=0 ; i < size; i++ ) {
			String zoneNow = whatZone(i);
			if ((!zoneNow.equals("All"))&&(!zoneNow.equals(zoneLast))) {
				zoneLast = zoneNow;
				numberOfZones ++;
				if (numberOfZones == 1) {
					endOfStartZone = i+1;
					zoneStart = zoneNow;
				} 
				if (numberOfZones == 2) {
					startOfEndZone = i;
					zoneEnd = zoneNow;
				} 
				System.out.println("i: " + i + " zoneNow: " + zoneNow);
			}
		}
		if (numberOfZones > 1) {
			makeZoneMap(endOfStartZone, zoneStart, startOfEndZone, zoneEnd);
			//System.out.println("numberOfZones: "+numberOfZones+
			//		" endOfStartZone: "+endOfStartZone+" startOfEndZone: "+startOfEndZone);
		} else {
			for(int i=0 ; i < size; i++ ) {
				String zone = whatZone(i);
				Set<String> zones = new TreeSet<>();
				if (!zone.equals("All")) { zones.add(zone);}
				else {
					zones.add("All");
				}
				zoneCalculation.add(zones);
			}
		}
		System.out.println(zoneCalculation);
	}
		
	private String whatZone(int i) {
		if ((i==0)&&(!startZone.equals("All"))) return startZone;
		if ((i==size)&&(!endZone.equals("All"))) return endZone;
		String countryCode = formChoosen.getCountry(i);
		if (!countryCode.equals("All")) {
			return rulesModule.getCountryZone(countryCode);
		}
		String airportName = formChoosen.getAirport(i);
		if (!airportName.equals("All")) {
			return rulesModule.getAirportZone(airportName);
		}
		return "All";
	}
	
	private void makeZoneMap(int start, String startZone, int end, String endZone) {
		Set<String> zonesA = new TreeSet<>();
		zonesA.add(startZone);
		for(int i=0 ; i < start; i++ ) {
			zoneCalculation.add(zonesA);
			System.out.println("i: "+ i + " zonesA: "+zonesA);
		}
		Set<String> zonesB = new TreeSet<>();
		zonesB.add(startZone);
		zonesB.add(endZone);
		for(int i=start ; i < end; i++ ) {
			zoneCalculation.add(zonesB);
			System.out.println("i: "+ i + " zonesB: "+zonesB);
		}
		Set<String> zonesC = new TreeSet<>();
		zonesC.add(endZone);
		for(int i=end ; i < size; i++ ) {
			zoneCalculation.add(zonesC);
			System.out.println("i: "+ i + " zonesC: "+zonesC);
		}
	}
	
}
