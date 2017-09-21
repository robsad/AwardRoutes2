package pl.robertsadlowski.awardroutes.app.data.rulesModule;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.data.Airports;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;

public class RulesModuleFactory {

	private Resources resources;
	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, List<Integer>> milesTable = new HashMap<>();
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private Airports airports;

	public RulesModuleFactory (Resources resources) {
		this.resources = resources;
	}

	public IRulesModule getModule(String programmeName, Map<String, String> countryByCode, Map<String,String> airlines) {
		String moduleName;
		switch(programmeName){
			case "MilesMore":
				moduleName="star_alliance";
				initModule(moduleName, countryByCode);
				break;
			case "AAdvantage":
				moduleName="one_world";
				initModule(moduleName, countryByCode);
				break;
			default:
				System.out.println("Not implemented module");
		}
		return new RulesMilesMore(connectionsByOrigin, airports, zoneNameList, countriesByZone, milesTable, airlines);
	}
	
	private void initModule(String moduleName, Map<String, String> countryByCode){
		loadZonesFromFile(moduleName);
		loadTableFromFile(moduleName);
		loadConnectionsFromFile(moduleName);
		List<AirportsData> airportsData = loadAirportsDataFromFile(moduleName);
		airports = new Airports(airportsData,countryByCode);
		}

	private int getId(String resourceName, Class<?> c) {
		try {
			System.out.println(resourceName);
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			throw new RuntimeException("No resource ID found for: "
					+ resourceName + " / " + c, e);
		}
	}

	private List<AirportsData> loadAirportsDataFromFile(String moduleName) {
		int resourceNr = getId(moduleName+"_airports", R.raw.class);
		InputStream inputStream = resources.openRawResource(resourceNr);
		List<AirportsData> airportsData = new ArrayList<>();
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				airportsData.add(new AirportsData(dataArray[0],dataArray[1],dataArray[2],
						Double.parseDouble(dataArray[3]),Double.parseDouble(dataArray[4])));
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
		return airportsData;
	}

	private void loadConnectionsFromFile(String moduleName) {
		int resourceNr = getId(moduleName+"_connections", R.raw.class);
		InputStream inputStream = resources.openRawResource(resourceNr);
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				String key = dataArray[0];
				List<Connection> connections = connectionsByOrigin.get(key);
				if (connections != null) {
					connections.add(new Connection(dataArray[1], dataArray[3], dataArray[2]));
					connectionsByOrigin.put(key, connections);
				} else {
					List<Connection> newConnections = new ArrayList<Connection>();
					newConnections.add(new Connection(dataArray[1], dataArray[3], dataArray[2]));
					connectionsByOrigin.put(key, newConnections);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
	}
	
	public void loadZonesFromFile(String moduleName) {
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
	
	public void loadTableFromFile(String moduleName) {
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


