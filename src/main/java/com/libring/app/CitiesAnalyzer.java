package com.libring.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.libring.app.datatypes.*;
import com.libring.app.sorter.MapSorter;
import com.libring.singleton.db.MongoDbHelper;

public class CitiesAnalyzer {

	private static final Logger LOGGER = Logger.getLogger(CitiesAnalyzer.class);

	private Map<String, List<City>> alphabeticalMap;
	private Map<String, List<City>> smallestPopulationMap;
	private Map<String, List<City>> biggestPopulationMap;
	private List<State> statesWithDistances;

	public void execute() {

		MongoDbHelper mongoDb = null;

		try {
			mongoDb = MongoDbHelper.getInstance();

			List<City> cityList = parseSrcJsonIntoCityList();
			mongoDb.insertCities(cityList);

			Map<String, List<City>> groupedMap = mongoDb.queryCitiesAlphabetically();

			alphabeticalMap = MapSorter.sortMapAlphabetically(groupedMap);
			smallestPopulationMap = MapSorter.sortMapBySmallestPopulation(groupedMap);
			biggestPopulationMap = MapSorter.sortMapByBiggestPopulations(groupedMap);
			statesWithDistances = MapSorter.createDistancesList(biggestPopulationMap);

		} catch (ExceptionInInitializerError | NullPointerException | JsonSyntaxException | IOException error) {
			LOGGER.fatal("An error occurred during the manipulation of the files. Please verify.", error);

		} finally {
			if (null != mongoDb) mongoDb.closeDb();
		}
	}

	public List<City> parseSrcJsonIntoCityList() throws NullPointerException, JsonSyntaxException {
		Reader reader = new InputStreamReader(CitiesAnalyzer.class.getResourceAsStream("/cities.json"));
		City[] cityArray = (new GsonBuilder().create()).fromJson(reader, City[].class);
		return Arrays.stream(cityArray).collect(Collectors.toList());
	}

	public Map<String, List<City>> getAlphabeticalMap() {
		return alphabeticalMap;
	}

	public Map<String, List<City>> getSmallestPopulationMap() {
		return smallestPopulationMap;
	}

	public Map<String, List<City>> getBiggestPopulationMap() {
		return biggestPopulationMap;
	}

	public List<State> getStatesWithDistances() {
		return statesWithDistances;
	}
}