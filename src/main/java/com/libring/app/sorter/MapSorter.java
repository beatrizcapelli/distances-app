package com.libring.app.sorter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.libring.app.datatypes.City;
import com.libring.app.datatypes.ComparableCity;
import com.libring.app.datatypes.State;
import com.libring.app.util.CsvWriterUtil;

/**
 * @brief Class used to sort the MAPs and output the results on both LOGGER and CSV file.
 *
 */
public class MapSorter {

	private static final Logger LOGGER = Logger.getLogger(MapSorter.class);

	public static SortedMap<String, List<City>> sortMapBySmallestPopulation(Map<String, List<City>> groupedMap) throws IOException {
		SortedMap<String, List<City>> smallestPopulationMap = new TreeMap<String, List<City>>(groupedMap);

		for (final List<City> list : smallestPopulationMap.values()) {
			list.sort(ComparableCity.Comparators.POPULATION_ASC);
			smallestPopulationMap.compute(list.get(0).getState(), (key, value) -> list.subList(0, Math.min(list.size(), 3)));
		}

		CsvWriterUtil.writeCityMap(smallestPopulationMap, "smallestPopulationMap.csv");
		LOGGER.debug("POPULATION_ASC MAP: " + smallestPopulationMap.toString());
		return smallestPopulationMap;
	}

	public static SortedMap<String, List<City>> sortMapByBiggestPopulations(Map<String, List<City>> groupedMap) throws IOException {
		SortedMap<String, List<City>> biggestPopulationMap = new TreeMap<String, List<City>>(groupedMap);

		for (final List<City> list : biggestPopulationMap.values()) {
			list.sort(ComparableCity.Comparators.POPULATION_DESC);
			biggestPopulationMap.compute(list.get(0).getState(), (key, value) -> list.subList(0, Math.min(list.size(), 2)));
		}

		CsvWriterUtil.writeCityMap(biggestPopulationMap, "biggestPopulationMap.csv");
		LOGGER.debug("POPULATION_DESC MAP: " + biggestPopulationMap.toString());
		return biggestPopulationMap;
	}

	public static SortedMap<String, List<City>> sortMapAlphabetically(Map<String, List<City>> groupedMap) {
		SortedMap<String, List<City>> alphabeticalMap = new TreeMap<String, List<City>>(groupedMap);
		LOGGER.debug("ALPHABETICAL MAP: " + alphabeticalMap.toString());
		return alphabeticalMap;
	}

	public static List<State> createDistancesList(Map<String, List<City>> biggestPopulationMap) throws IOException {

		List<State> statesWithDistances = new ArrayList<State>();

		biggestPopulationMap.forEach((key, values) -> { 
			try { 
				statesWithDistances.add(new State(key, values.get(0), values.get(1)));
			} catch (IndexOutOfBoundsException error) {
				LOGGER.debug("State " + key + " does not have 2 cities listed");
			}
		});

		statesWithDistances.sort(State.Comparators.DIST_BIGGEST_CITIES);

		CsvWriterUtil.writeStateList(statesWithDistances, "statesWithDistances.csv");
		LOGGER.debug("STATE WITH SMALLEST DISTANCE BETWEEN ITS BIGGEST CITIES: " + statesWithDistances.get(0));
		return statesWithDistances;
	}
}