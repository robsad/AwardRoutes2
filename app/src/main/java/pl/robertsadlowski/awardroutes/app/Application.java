package pl.robertsadlowski.awardroutes.app;

import android.content.res.Resources;

import java.util.Map;

import pl.robertsadlowski.awardroutes.app.data.DataLoader;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.RulesModule;
import pl.robertsadlowski.awardroutes.app.data.rulesModule.RulesModuleFactory;
import pl.robertsadlowski.awardroutes.app.logic.ContainerManager;

public class Application {

	Resources resources;
	private Map<String,String> airlines;
	private Map<String, String> countryByCode;

	public Application (Resources resources) {
		this.resources = resources;
		DataLoader dataLoader = new DataLoader(resources);
		countryByCode = dataLoader.getCountryCodes();
		airlines = dataLoader.getAirlines();
	}

    public ContainerManager getContainerManager(String programmeName)
    {
    	RulesModuleFactory rulesFactory = new RulesModuleFactory(resources);
    	RulesModule rulesModule = rulesFactory.getModule(programmeName, countryByCode, airlines);
		ContainerManager containerManager = new ContainerManager(rulesModule);
		return containerManager;
    }

}
