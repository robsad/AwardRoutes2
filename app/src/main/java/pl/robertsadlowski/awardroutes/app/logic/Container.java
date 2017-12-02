package pl.robertsadlowski.awardroutes.app.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.airports.Airports;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.MileageLevels;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.RulesModule;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.ZoneFilter;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;
import pl.robertsadlowski.awardroutes.app.gateaway.FormPossibles;

public class Container {

	public static final String ANY_AIRPORT = "Any airport";
	public static final String ANY_COUNTRY = "Any country";
	public static final String ANY_ZONE = "Any zone";
	private List<RouteLine> routeLines = new ArrayList<>();
	private RulesModule rulesModule;
	private Airports airports;
	private final int size;

	public Container(int size, RulesModule rulesModule) {
		this.size = size;
		this.rulesModule = rulesModule;
		this.airports = rulesModule.getAirports();
	}

	public FormChoosen getFormChoosen() {
		return new FormChoosen(size);
	}

	public FormPossibles calculateRoutes(FormChoosen formChoosen) {
		ZoneFilter zonefilter = rulesModule.getZoneFilterInstance();
		zonefilter.calculateZonesAndConfigureFilter(formChoosen);
		setRouteLines(formChoosen);
		FormPossibles formPossibles = calculateRoutesIntersections(formChoosen);
		MileageLevels mileageNeeded = rulesModule.getMilesNeeded(formChoosen);
		formPossibles.setMileageNeeded(mileageNeeded);
        String zoneStart = zonefilter.getStartZone();
        String zoneEnd = zonefilter.getEndZone();
		formPossibles.setZoneStart(zoneStart);
		formPossibles.setZoneEnd(zoneEnd);
		formPossibles.setAirlines(getAirlines(formChoosen));
		formPossibles.setMessage(rulesModule.getMessage(size,mileageNeeded,zoneStart,zoneEnd));
		formPossibles.setChoosenPortsCodes(getCodes(formChoosen));
		return formPossibles;
	}

	private void setRouteLines(FormChoosen formChoosen) {
		routeLines.clear();
		for (int i = 0; i < size; i++) {
			routeLines.add(new RouteLine(i, formChoosen, rulesModule));
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
				airlines.add(rulesModule.getAirline(choosenAirportLast,choosenAirportThis));
			} else {
				airlines.add("");
			}
		}
		airlines.add("");
		return airlines;
	}

	private List<String> getCodes(FormChoosen formChoosen) {
		List<String> codes = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			String choosenAirport = formChoosen.getAirport(i);
			if (!choosenAirport.equals(ANY_AIRPORT)) {
				codes.add(airports.getAirportCodeByName(choosenAirport));
			} else {
				codes.add(ANY_AIRPORT);
			}
		}
		return codes;
	}

}
