package pl.robertsadlowski.awardroutes.app.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.app.data.airports.AbstractAirports;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.IRulesModule;
import pl.robertsadlowski.awardroutes.app.gateaway.FormChoosen;

public class RouteLine {

	private final List<Set<String>> routeLineList = new ArrayList<>();
	private final Map<String, List<Connection>> connectionsByOrigin;
	private final IRulesModule rulesModule;
	private final AbstractAirports abstractAirports;
	private final FormChoosen formChoosen;
	private final int size;
	private final int routeNr;

	public RouteLine(int size, int routeNr, FormChoosen formChoosen, IRulesModule rulesModule) {
		this.size = size;
		this.routeNr = routeNr;
		this.formChoosen = formChoosen;
		this.abstractAirports = rulesModule.getAirports();
		this.rulesModule = rulesModule;
		this.connectionsByOrigin = rulesModule.getConnectionsByOrigin();
		init();
	}

	public Set<String> getRouteLineStop(int stopNr) {
		return routeLineList.get(stopNr);
	}

	public boolean isRouteLineActive() {
		//System.out.println("!!!routeLineList: "+routeNr+ " "+!routeLineList.isEmpty() + " " + routeLineList);
		if (routeLineList.isEmpty()) return false;
		return true;
	}

	private void init() {
		Set<String> initAirportNames = getInitAirports();
		if (!initAirportNames.isEmpty()) {
			for(int i=0 ; i < size; i++ ) {
				routeLineList.add(new TreeSet<String>());
			}
			calculate(initAirportNames);
		}
	}

	private Set<String> getInitAirports() {
		String choosenAirport = formChoosen.getAirport(routeNr);
		Set<String> initAirports = new TreeSet<>();
		if (!choosenAirport.equals(Container.ANY_AIRPORT)) {
			initAirports.add(choosenAirport);
			return initAirports;
		}
		String choosenCountry = formChoosen.getCountry(routeNr);
		if (!choosenCountry.equals(Container.ANY_COUNTRY)) {
			return abstractAirports.getAirportsByCountry(choosenCountry);
		}
		String zone="";
		if ((routeNr==0)&&(!formChoosen.getStartZone().equals(Container.ANY_ZONE))) zone = formChoosen.getStartZone();
		if ((routeNr==size-1)&&(!formChoosen.getEndZone().equals(Container.ANY_ZONE))) zone = formChoosen.getEndZone();
		if (!zone.equals("")) return rulesModule.getAirportsByZone(zone);
		return Collections.emptySet();
	}

	private void calculate(Set<String> neighbours) {
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
		System.out.println("routeNr:"+routeNr);
		for(int i=0 ; i < size; i++ ) {
			System.out.println("Leg nr: " + i + routeLineList.get(i));
		}
		System.out.println("");
	}

	private boolean isEdge(int positionL, int positionR){
		if ((positionL<1)&&(positionR>size)) return false;
		else return true;
	}

	private Set<String> calculateNeighbors(Set<String> initAirportNames) {
		Set<String> neighbors = new TreeSet<>();
		for(String initAirportName : initAirportNames) {
			String initAirportCode = abstractAirports.getAirportCodeByName(initAirportName);
			List<Connection> connections = connectionsByOrigin.get(initAirportCode);
			if (connections!=null) {
				for (Connection connection : connections) {
					String connectionName = abstractAirports.getAirportNameByCode(connection.getDestination());
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
				if (possibleZones.contains(Container.ANY_ZONE)) newNeighbours.add(airport);
			}
			neighbours = newNeighbours;
		}
		routeLineList.set(position,neighbours);
	}

	private boolean isActiveFilter() {
		return formChoosen.isZoneRule();
	}

	@Override
	public String toString() {
		return routeLineList.toString();
	}
}
