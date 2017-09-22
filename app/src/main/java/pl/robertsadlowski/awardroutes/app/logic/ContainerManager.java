package pl.robertsadlowski.awardroutes.app.logic;

import pl.robertsadlowski.awardroutes.app.data.airports.AbstractAirports;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.IRulesModule;

public class ContainerManager {
	
	private IRulesModule rulesModule;
	private Container container;
	
	public ContainerManager(IRulesModule rulesModule){
		this.rulesModule = rulesModule;
	}
	
	public Container addCountainer(int size) {
		return new Container(size, rulesModule);
	}

	public AbstractAirports getAirportsModule() {
		return rulesModule.getAirports();
	}

}
