package pl.robertsadlowski.awardroutes.app.data;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.robertsadlowski.awardroutes.R;
import pl.robertsadlowski.awardroutes.app.data.entities.AirportsData;
import pl.robertsadlowski.awardroutes.app.data.entities.Connection;


public class DataLoader {

	Resources resources;

	public DataLoader (Resources resources) {
		this.resources = resources;
	}

	public List<AirportsData> getAirports() {
		InputStream inputStream = resources.openRawResource(R.raw.star_alliance_airports);
		return loadAirportsFromFile(inputStream);
	}

	public Map<String, List<Connection>> getConnections() {
		InputStream inputStream = resources.openRawResource(R.raw.star_alliance_connections);
		return loadConnectionsFromFile(inputStream);
	}

	public Map<String, String> getCountryCodes() {
		InputStream inputStream = resources.openRawResource(R.raw.countries);
		return loadCountriesFromFile(inputStream);
	}

	private List<AirportsData> loadAirportsFromFile(InputStream inputStream) {
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

	private Map<String,List<Connection>> loadConnectionsFromFile(InputStream inputStream) {
		Map<String, List<Connection>> connectionsByOrigin = new HashMap<>();
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
		return connectionsByOrigin;
	}

	private Map<String, String> loadCountriesFromFile(InputStream inputStream) {
		Map<String, String> countryByCode = new HashMap<>();
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				countryByCode.put(dataArray[0], dataArray[1]);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
		return countryByCode;
	}

}
