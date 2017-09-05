package pl.robertsadlowski.awardroutes.app.data.entities;

import java.io.Serializable;
import java.sql.Timestamp;

//import javax.persistence.*;

public class AirportsData implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private String cityCode;

	private String cityName;
	
	private String countryCode;

	private Timestamp created;

	private double lat;

	private double lon;

	private Timestamp modified;
	
	public AirportsData(String cityCode, String cityName, String countryCode,
			double lat, double lon) {
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.countryCode = countryCode;
		this.lat = lat;
		this.lon = lon;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public double getLon() {
		return this.lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public Timestamp getModified() {
		return this.modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	@Override
	public String toString(){
		return "Code: " + cityCode + 
				", Name: " + cityName + 
				", Country: " + countryCode;
	}

}

