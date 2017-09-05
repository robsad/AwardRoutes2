package pl.robertsadlowski.awardroutes.app;

import android.content.res.Resources;

import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.Airports;
import pl.robertsadlowski.awardroutes.app.data.DataLoader;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.IRulesModule;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.RulesModuleFactory;
import pl.robertsadlowski.awardroutes.app.logic.ContainerManager;

public class Application
{
	Resources resources;

	public Application (Resources resources) {
		this.resources = resources;
	}

    public ContainerManager getContainerManager()
    {
    	DataLoader dataLoader = new DataLoader(resources);
    	List<AirportsData> airportsData = dataLoader.getAirports();
    	Map<String, String> countryByCode = dataLoader.getCountryCodes();
		Map<String, List<Connection>> connectionsByOrigin = dataLoader.getConnections();
    	Airports airports = new Airports(airportsData,countryByCode);

    	RulesModuleFactory rulesFactory = new RulesModuleFactory(resources);
    	IRulesModule milesMoreModule = rulesFactory.getModule("MilesMore", airports, connectionsByOrigin);
		ContainerManager containerManager = new ContainerManager(milesMoreModule);
		return containerManager;
    }

}
