package com.libring.app;

import org.apache.log4j.Logger;

public class Main 
{
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		CitiesAnalyzer citiesAnalyzer = new CitiesAnalyzer();
		citiesAnalyzer.execute();

		LOGGER.info("RUN TIME: " + (System.currentTimeMillis() - startTime));
	}
}