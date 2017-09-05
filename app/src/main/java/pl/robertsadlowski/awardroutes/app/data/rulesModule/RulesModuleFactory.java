package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.data.Airports;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;

public class RulesModuleFactory {

	private Resources resources;
	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, List<Integer>> milesTable = new HashMap<>();

	public RulesModuleFactory (Resources resources) {
		this.resources = resources;
	}

	public IRulesModule getModule(String moduleName, Airports airports, Map<String, List<Connection>> connectionsByOrigin) {
		initModule();
		//if (moduleName.equals("MilesMore") {}
		IRulesModule rulesModule = new RulesMilesMore(connectionsByOrigin, airports, zoneNameList, countriesByZone, milesTable);
		return rulesModule;
	}
	
	public void initModule(){
			loadZonesFromFile();
			loadTableFromFile();
			//System.out.println("miles table loaded: " + milesTable);
		}
	
	
	public void loadZonesFromFile() {
		InputStream inputStream = resources.openRawResource(R.raw.zones);
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				List<String> countryCodes = Arrays.asList(Arrays.copyOfRange(dataArray, 1, dataArray.length));
				countriesByZone.put(dataArray[0], countryCodes);
				zoneNameList.add(dataArray[0]);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
	}
	
	public void loadTableFromFile() {
		InputStream inputStream = resources.openRawResource(R.raw.table);
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				List<String> milesNeededListString = Arrays.asList(Arrays.copyOfRange(dataArray, 1, dataArray.length));
				List<Integer> milesNeededList = new LinkedList<>();
				for(String value : milesNeededListString) milesNeededList.add(Integer.valueOf(value));
				milesTable.put(dataArray[0], milesNeededList);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
	}



	
}


