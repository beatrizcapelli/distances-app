package com.libring.app.datatypes;

import java.util.Comparator;

/**
 * @brief Comparable class for City - implemented separately for maintenance of the POJO pattern on City class.
 * 
 */
public class ComparableCity extends City implements Comparable<City> {

	@Override
	public int compareTo(City city) {
		return Comparators.ALPHABETICAL.compare(this, city);
	}

	public static class Comparators {

		public static Comparator<City> ALPHABETICAL = new Comparator<City>() {
			@Override
			public int compare(City city1, City city2) {
				return city1.toString().compareTo(city2.toString());
			}
		};

		public static Comparator<City> POPULATION_DESC = new Comparator<City>() {
			@Override
			public int compare(City city1, City city2) {
				return city2.getPopulation().compareTo(city1.getPopulation());
			}
		};

		public static Comparator<City> POPULATION_ASC = new Comparator<City>() {
			@Override
			public int compare(City city1, City city2) {
				return city1.getPopulation().compareTo(city2.getPopulation());
			}
		};
	}

}
