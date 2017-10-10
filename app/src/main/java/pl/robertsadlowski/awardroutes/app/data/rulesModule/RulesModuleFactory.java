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

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;

public class RulesModuleFactory {

	private Resources resources;
	private List<String> zoneNameList = new ArrayList<>();
	private Map<String, List<String>> countriesByZone = new HashMap<>();
	private Map<String, List<MileageLevels>> milesTable = new HashMap<>();
	private Map<String, MileageLevels> milesTableDomestic = new HashMap<>();
	private Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
	private List<AirportsData> airportsData = new ArrayList<>();

	public RulesModuleFactory (Resources resources) {
		this.resources = resources;
	}

	public RulesModule getModule(String programmeName, Map<String, String> countryByCode, Map<String,String> airlines) {
		switch(programmeName){
			case "MilesMore":
				initMilesMore(countryByCode);
				return new RulesMilesMore(connectionsByOrigin,airportsData,countryByCode,zoneNameList,countriesByZone,milesTable,milesTableDomestic,airlines);
			case "AAdvantage":
				initAAdvantage(countryByCode);
				return new RulesAAdvantage(connectionsByOrigin,airportsData,countryByCode,zoneNameList,countriesByZone,milesTable,milesTableDomestic,airlines);
			default:
				System.out.println("Not implemented module");
		}
		return null;
	}
	
	private void initMilesMore(Map<String, String> countryByCode){
		loadZonesFromFile("miles_more");
		loadTableFromFile("miles_more");
		loadConnectionsFromFile("star_alliance");
		loadAirportsDataFromFile("star_alliance");
		}

	private void initAAdvantage(Map<String, String> countryByCode){
		loadZonesFromFile("aadvantage");
		loadTableFromFile("aadvantage");
		loadConnectionsFromFile("one_world");
		loadConnectionsFromFile("aadvantage");
		loadAirportsDataFromFile("one_world");
		loadAirportsDataFromFile("aadvantage");
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

	private void loadAirportsDataFromFile(String moduleName) {
		int resourceNr = getId(moduleName+"_airports", R.raw.class);
		InputStream inputStream = resources.openRawResource(resourceNr);
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
		int resourceNr = getId(moduleName+"_zones", R.raw.class);
		InputStream inputStream = resources.openRawResource(resourceNr);
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				List<String> countryCodes = Arrays.asList(Arrays.copyOfRange(dataArray, 1, dataArray.length));
				countriesByZone.put(dataArray[0], countryCodes);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
	}
	
	public void loadTableFromFile(String moduleName) {
		int resourceNr = getId(moduleName+"_table", R.raw.class);
		InputStream inputStream = resources.openRawResource(resourceNr);
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
                String marker = dataArray[0];
                switch (marker) {
                    case "header":
                        break;
                    case "domestic":
                        String country = dataArray[1].toUpperCase();
                        milesTableDomestic.put(country, new MileageLevels(dataArray[3],dataArray[4]));
                        break;
                    default:
                        List<MileageLevels> milesNeededList = new ArrayList<>();
						for (int i=1;i<dataArray.length;i+=2) {
							milesNeededList.add(new MileageLevels(dataArray[i],dataArray[i+1]));
						}
						milesTable.put(marker, milesNeededList);
						zoneNameList.add(marker);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
	}
	
}


