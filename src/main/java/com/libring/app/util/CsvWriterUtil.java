package com.libring.app.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.libring.app.datatypes.City;
import com.libring.app.datatypes.State;

public class CsvWriterUtil {

	private static final Logger LOGGER = Logger.getLogger(CsvWriterUtil.class);
	
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char LINE_ENDING = '\n';
	
	public static void writeCityMap(SortedMap<String, List<City>> map, String fileName) throws IOException {

		FileWriter writer = createCsvFile(fileName);
	    
	    boolean first = true;
		StringBuilder builder = new StringBuilder();

		createMapHeader(builder);
		
		for (Map.Entry<String, List<City>> entry : map.entrySet()) {
			if (!first) {
				builder.append(DEFAULT_SEPARATOR);
			}

			builder.append(entry.getKey());
			builder.append(DEFAULT_SEPARATOR);
			builder.append(entry.getValue().stream().map(x -> x.getCity()).collect(Collectors.joining("; ")));
			builder.append(LINE_ENDING);
		}
		
		closeWriter(writer, builder);
	}

	public static void createMapHeader(StringBuilder builder) {
		builder.append("STATE");
		builder.append(DEFAULT_SEPARATOR);
		builder.append("CITIES");
		builder.append(LINE_ENDING);
	}
	
	public static void writeStateList(List<State> statesWithDistances, String fileName) throws IOException {
		FileWriter writer = createCsvFile(fileName);
		
		boolean first = true;
		StringBuilder builder = new StringBuilder();
		
		createStateListHeader(builder);
		
		for (State state : statesWithDistances) {
			if (!first) {
				builder.append(DEFAULT_SEPARATOR);
			}

			builder.append(state.getState());
			builder.append(DEFAULT_SEPARATOR);
			builder.append(state.getFormattedStrDistance());
			builder.append(LINE_ENDING);
		}
		
		closeWriter(writer, builder);
	}

	public static void createStateListHeader(StringBuilder builder) {
		builder.append("STATE");
		builder.append(DEFAULT_SEPARATOR);
		builder.append("DISTANCE BETWEEN BIGGEST CITIES (KM)");
		builder.append(LINE_ENDING);
	}

	public static FileWriter createCsvFile(String fileName) throws IOException {
		Path currentRelativePath = Paths.get("");
		String filePath = currentRelativePath.toAbsolutePath().toString() + File.separator + fileName;
	    FileWriter writer = new FileWriter(filePath);
	    LOGGER.info("Creating CSV file at: " + filePath);
		return writer;
	}

	public static void closeWriter(FileWriter writer, StringBuilder builder)
			throws IOException {
		builder.append(LINE_ENDING);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
	}
}