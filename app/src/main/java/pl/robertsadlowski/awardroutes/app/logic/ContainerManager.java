package pl.robertsadlowski.awardroutes.app.logic;

import pl.robertsadlowski.awardroutes.app.data.airports.Airports;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.RulesModule;

public class ContainerManager {
	
	private RulesModule rulesModule;
	private Container container;
	
	public ContainerManager(RulesModule rulesModule){
		this.rulesModule = rulesModule;
	}
	
	public Container addCountainer(int size) {
		return new Container(size, rulesModule);
	}

	public Airports getAirportsModule() {
		return rulesModule.getAirports();
	}

}
