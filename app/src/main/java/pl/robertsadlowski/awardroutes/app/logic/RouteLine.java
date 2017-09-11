package pl.robertsadlowski.awardroutes.app.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.Airports;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.IRulesModule;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;

public class RouteLine {

	private final List<Set<String>> routeLineList = new ArrayList<>();
	private final Map<String, List<Connection>> connectionsByOrigin;
	private final IRulesModule rulesModule;
	private final Airports airports;
	private final FormChoosen formChoosen;
	private final int size;
	private final int routeNr;

	public RouteLine(int size, int routeNr, FormChoosen formChoosen, IRulesModule rulesModule) {
		this.size = size;
		this.routeNr = routeNr;
		this.formChoosen = formChoosen;
		this.airports = rulesModule.getAirports();
		this.rulesModule = rulesModule;
		this.connectionsByOrigin = rulesModule.getConnectionsByOrigin();
		init();
	}

	public Set<String> getRouteLineStop(int stopNr) {
		return routeLineList.get(stopNr);
	}

	public void init() {
		for(int i=0 ; i < size; i++ ) {
			routeLineList.add(new TreeSet<String>());
		}
		Set<String> initAirportNames = getInitAirports();
		System.out.println("initAirportNames: " + initAirportNames);
		if (!initAirportNames.isEmpty()) {
			calculate(initAirportNames);
		}
	}

	public void calculate(Set<String> neighbours) {
		int positionL = routeNr;
		int positionR = routeNr;
		routeLineList.set(routeNr,neighbours);
		while (isEdge(positionL,positionR)) {
			positionL--;
			positionR++;
			Set<String> newNeighbours = calculateNeighbors(neighbours);
			if (positionL>=0) zoneFilter(positionL,newNeighbours);
			if (positionR<size) zoneFilter(positionR,newNeighbours);
			neighbours = newNeighbours;
		}
	}

	public Set<String> calculateNeighbors(Set<String> initAirportNames) {
		Set<String> neighbors = new TreeSet<>();
		for(String initAirportName : initAirportNames) {
			String initAirportCode = airports.getAirportCodeByName(initAirportName);
			List<Connection> connections = connectionsByOrigin.get(initAirportCode);
			if (connections!=null) {
				for (Connection connection : connections) {
					String connectionName = airports.getAirportNameByCode(connection.getDestination());
					neighbors.add(connectionName);
				}
			}
		}
		return neighbors;
	}

	private void zoneFilter(int position, Set<String> neighbours) {
		if (isActiveFilter()) {
			Set<String> possibleZones = formChoosen.getZoneCalculation(position);
			Set<String> newNeighbours = new TreeSet<>(Collections.<String>emptySet());
			for (String airport : neighbours) {
				String thisZone = rulesModule.getAirportZone(airport);
				if (possibleZones.contains(thisZone)) newNeighbours.add(airport);
				if (possibleZones.contains("All")) newNeighbours.add(airport);
			}
			neighbours = newNeighbours;
		}
		routeLineList.set(position,neighbours);
	}

	private boolean isEdge(int positionL, int positionR){
		if ((positionL<1)&&(positionR>size)) return false;
		else return true;
	}

	private boolean isActiveFilter() {
		return formChoosen.isZoneRule();
	}

	private Set<String> getInitAirports() {
		String choosenAirport = formChoosen.getAirport(routeNr);
		Set<String> initAirports = new TreeSet<>();
		if (!choosenAirport.equals("All")) {
			initAirports.add(choosenAirport);
			System.out.println("choosenAirport " + choosenAirport);
			return initAirports;
		}
		String choosenCountry = formChoosen.getCountry(routeNr);
		if (!choosenCountry.equals("All")) {
			System.out.println("choosenCountry " + choosenCountry);
			System.out.println("choosenAirports " + airports.getAirportsByCountryName(choosenCountry));
			return airports.getAirportsByCountryName(choosenCountry);
		}
		System.out.println("emptyset -> choosenAirport " + choosenAirport);
		return Collections.<String>emptySet();
	}

	@Override
	public String toString() {
		return routeLineList.toString();
	}
}
