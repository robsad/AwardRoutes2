package pl.robertsadlowski.awardroutes.app.gateaway;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.rulesModule.MileageLevels;

public class FormPossibles {

	private final int size;
	private List<Set<String>> possiblePorts = new ArrayList<>();
	private List<String> choosenPortsCodes = new ArrayList<>();
	private List<Set<String>> possibleCountries = new ArrayList<>();
	private List<Set<String>> possibleZones = new ArrayList<>();
	private List<String> airlinesList = new ArrayList<>();
	private MileageLevels mileageLevels;
	private String zoneStart;
	private String zoneEnd;
	private String message=null;

	public FormPossibles(int size) {
		this.size=size;
		for(int i=0 ; i < size; i++ ) {
			possiblePorts.add(new TreeSet<String>());
			possibleCountries.add(new TreeSet<String>());
			possibleZones.add(new TreeSet<String>());
		}
	}

	public int getSize() {
		return size;
	}

	public void setZoneStart(String zoneStart) {
		this.zoneStart = zoneStart;
	}

	public String getZoneStart() {
		return zoneStart;
	}

	public void setZoneEnd(String zoneEnd) {
		this.zoneEnd = zoneEnd;
	}

	public String getZoneEnd() {
		return zoneEnd;
	}

	public void setAirports(int i, Set<String> airports) {
		possiblePorts.set(i, airports);
	}

	public void setChoosenPortsCodes(List<String> airportCodes) {
		this.choosenPortsCodes=airportCodes;
	}

	public String getChoosenPortsCodes(int i) {
		return choosenPortsCodes.get(i);
	}

	public Set<String> getAirports(int i) {
		return possiblePorts.get(i);
	}

	public void setCountries(int i, Set<String> countries) {
		possibleCountries.set(i, countries);
	}

	public Set<String> getCountries(int i) {
		return possibleCountries.get(i);
	}

	public void setZones(int i, Set<String> zones) {
		possibleZones.set(i, zones);
	}

	public Set<String> getZones(int i) {
		return possibleZones.get(i);
	}

	public void setMileageNeeded(MileageLevels mileageLevels) {this.mileageLevels = mileageLevels;}

	public MileageLevels getMileageNeeded() { return mileageLevels;}

	public void setAirlines(List<String> airlinesList) {
		this.airlinesList = airlinesList;
	}

	public String getAirline(int i) {
		return airlinesList.get(i);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		return possiblePorts.toString()
				+ "\n" + possibleCountries.toString()
				+ "\n" + possibleZones.toString()
				+ "\n" + "Start:" + zoneStart + " End:" + zoneEnd
				+ "\n Mileage:" + getMileageNeeded();
	}
}


