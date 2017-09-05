package pl.robertsadlowski.awardroutes.app.gateaway;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FormPossibles {

	private final int size;
	private List<Set<String>> possiblePorts = new ArrayList<>();
	private List<Set<String>> possibleCountries = new ArrayList<>();
	private List<List<String>> possibleZones = new ArrayList<>();
	private int mileageNeeded;
	
	public FormPossibles(int size) {
		this.size=size;
		for(int i=0 ; i < size; i++ ) {
			possiblePorts.add(new TreeSet<String>());
			possibleCountries.add(new TreeSet<String>());
		}
	}
	
	public void setAirports(int i, Set<String> airports) {
		possiblePorts.set(i, airports);
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
	
	public void setZones(int i, List<String> zones) {
		possibleZones.set(i, zones);
	}

	public List<String> getZones(int i) {
		return possibleZones.get(i);
	}

	public void setMileageNeeded(int i) {this.mileageNeeded = i;}

	public int getMileageNeeded() { return mileageNeeded;}

	@Override
	public String toString() {
		return possiblePorts.toString();
	}
}

