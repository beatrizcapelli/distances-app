package com.libring.app.datatypes;

/**
 * @brief POJO City
 *
 */
public class City {

	private String city;
	private double latitude = 0;
	private double longitude = 0;
	private Integer population = 0;
	private String state;

	public City() {

	}

	public City(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return city;
	}

	public String getState() {
		return state;
	}

	public double getLatitude() {
		return latitude;
	}

	public Integer getPopulation() {
		return population;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static final class CityConstants {
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String LONGITUDE = "longitude";
		public static final String LATITUDE = "latitude";
		public static final String POPULATION = "population";
	}
}