package pl.robertsadlowski.awardroutes.app.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.Airports;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.IRulesModule;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.IZoneFilter;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;

public class Container {

	public static final String ANY_AIRPORT = "Any airport";
	public static final String ANY_COUNTRY = "Any country";
	public static final String ANY_ZONE = "Any zone";
	private List<RouteLine> routeLines = new ArrayList<>();
	private IRulesModule rulesModule;
	private Airports airports;
	private final int size;

	public Container(int size, IRulesModule rulesModule) {
		this.size = size;
		this.rulesModule = rulesModule;
		this.airports = rulesModule.getAirports();
	}

	public FormChoosen getFormChoosen() {
		return new FormChoosen(size);
	}

	public FormPossibles calculateRoutes(FormChoosen formChoosen) {
		IZoneFilter zonefilter = rulesModule.getZoneFilterInstance();
		List<Set<String>> zoneCalculated = zonefilter
				.calculateZones(formChoosen);
		formChoosen.setZoneCalculation(zoneCalculated);
		setRouteLines(formChoosen);
		FormPossibles formPossibles = calculateRoutesIntersections(formChoosen);
		String zoneStart = zonefilter.getStartZone();
		String zoneEnd = zonefilter.getEndZone();
		int mileageNeeded = rulesModule.getMilesNeeded(zoneStart,zoneEnd);
		formPossibles.setMileageNeeded(mileageNeeded);
		formPossibles.setZoneStart(zoneStart);
		formPossibles.setZoneEnd(zoneEnd);
		formPossibles.setAirlines(getAirlines(formChoosen));
		formPossibles.setMessage(rulesModule.getMessage(size,mileageNeeded,zoneStart,zoneEnd));
		return formPossibles;
	}

	private void setRouteLines(FormChoosen formChoosen) {
		routeLines.clear();
		for (int i = 0; i < size; i++) {
			routeLines.add(new RouteLine(size, i, formChoosen, rulesModule));
		}
	}

	private FormPossibles calculateRoutesIntersections(FormChoosen formChoosen) {
		FormPossibles formPossibles = new FormPossibles(size);
		for (int i = 0; i < size; i++) {
			Set<String> intersection;
			if (formChoosen.isNothingChoosen()) {
				intersection = airports.getAllAirportNames();
			} else {
				intersection = intersection(i,formChoosen);
			}
			Set<String> possibleCountries = calculatePossibleCountry(formChoosen.getAirport(i), intersection);
			formPossibles.setCountries(i, possibleCountries);
			String choosenCountry = formChoosen.getCountry(i);
			formPossibles.setZones(i, calculatePossibleZones(choosenCountry,possibleCountries));
			if (!choosenCountry.equals(ANY_COUNTRY)) {
				intersection.retainAll(airports.getAirportsByCountry(choosenCountry));
			}
			formPossibles.setAirports(i, intersection);
		}
		return formPossibles;
	}

	private Set<String> calculatePossibleCountry(String choosenAirport, Set<String> possiblePorts) {
		Set<String> possibleCountries = new TreeSet<String>();
		if (!choosenAirport.equals(ANY_AIRPORT)) {
			possibleCountries.add(airports.getAirportsCountryName(choosenAirport));
		} else {
			for (String port : possiblePorts) {
				possibleCountries.add(airports.getAirportsCountryName(port));
			}
		}
		return new TreeSet<String>(possibleCountries);
	}

	private Set<String> calculatePossibleZones(String choosenCountry, Set<String> possibleCoutries) {
		Set<String> possibleZones = new TreeSet<String>();
		if (!choosenCountry.equals(ANY_COUNTRY)) {
			possibleZones.add(rulesModule.getCountryNameZone(choosenCountry));
		} else {
			for (String country : possibleCoutries) {
				possibleZones.add(rulesModule.getCountryNameZone(country));
			}
		}
		return  new TreeSet<String>(possibleZones);
	}

	private Set<String> intersection(int i,FormChoosen formChoosen) {
		String choosenAirport = formChoosen.getAirport(i);
		String choosenCountry = formChoosen.getCountry(i);
		Set<String> intersection = null;
		for(int j=0 ; j < size; j++ ) {
			RouteLine routeLine = routeLines.get(j);
			if (routeLine.isRouteLineActive()) {
				Set<String> routeStop = routeLine.getRouteLineStop(i);
					if (((i==j)&&((!choosenAirport.equals(ANY_AIRPORT))||(!choosenCountry.equals(ANY_COUNTRY))))) {
					routeStop = airports.getAllAirportNames();
				}
				if (intersection==null) {
					intersection=routeStop;
				} else {
					intersection.retainAll(routeStop);
				}
			}
		}
		return new TreeSet<String>(intersection);
	}

	private List<String> getAirlines(FormChoosen formChoosen) {
		List<String> airlines = new ArrayList<String>();
		for (int i = 1; i < size; i++) {
			String choosenAirportLast = formChoosen.getAirport(i-1);
			String choosenAirportThis = formChoosen.getAirport(i);
			if ((!choosenAirportLast.equals(ANY_AIRPORT))&&(!choosenAirportThis.equals(ANY_AIRPORT))) {
				airlines.add("Flight " + i + " operated by " + rulesModule.getAirline(choosenAirportLast,choosenAirportThis));
			} else {
				airlines.add("Flight " + i );
			}
		}
		airlines.add("");
		return airlines;
	}

}
