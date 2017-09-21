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

import static pl.robertsadlowski.awardroutes.R.raw.airlines;


public class DataLoader {

	Resources resources;

	public DataLoader (Resources resources) {
		this.resources = resources;
	}

	public Map<String, String> getCountryCodes() {
		InputStream inputStream = resources.openRawResource(R.raw.countries);
		return loadCountriesFromFile(inputStream);
	}

	public Map<String, String> getAirlines() {
		InputStream inputStream = resources.openRawResource(airlines);
		return loadAirlinesFromFile(inputStream);
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

	public Map<String,String> loadAirlinesFromFile(InputStream inputStream) {
		Map<String,String> airlines = new HashMap<>();
		String csvLine;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((csvLine = br.readLine()) != null) {
				String[] dataArray = csvLine.split(";");
				airlines.put(dataArray[0], dataArray[1]);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		}
		return airlines;
	}

}
