package com.libring.singleton.db;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.libring.app.datatypes.City;
import com.libring.singleton.db.util.MongoDbUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public final class MongoDbHelper {

	private static final Logger LOGGER = Logger.getLogger(MongoDbHelper.class);
	private static final MongoDbHelper INSTANCE;
	
    static {
        try {
            INSTANCE = new MongoDbHelper();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	private MongoDbHelper() throws IOException, NumberFormatException {
		Properties config = MongoDbUtil.getMongoDbProperties();
		String host = config.getProperty(MongoDbUtil.DB_HOST);
		int port = Integer.parseInt(config.getProperty(MongoDbUtil.DB_PORT));
		String name = config.getProperty(MongoDbUtil.DB_NAME);

		LOGGER.info("Opening DB connection (" + name + ") at " + host + ":" + port);
		initializeMongoClient(host, port, name);
	}
	
	public static MongoDbHelper getInstance() {
		return INSTANCE;
	}

	public void closeDb() {
		LOGGER.info("Closing DB connection.");
		mongoClient.close();
	}
	
	/**
	 * @brief Initializes the MongoClient using POJO Codec to be able to map "City" class without additional libraries.
	 * 
	 */
	public void initializeMongoClient(String host, int port, String name) {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(City.class).build();
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

		MongoClientOptions options = MongoClientOptions.builder()
				.codecRegistry(pojoCodecRegistry).build();

		mongoClient = new MongoClient(new ServerAddress(host, port), options);  
		mongoDatabase = mongoClient.getDatabase(name).withCodecRegistry(pojoCodecRegistry);
		LOGGER.info("DB connection opened.");
	}

	public void insertCities(List<City> cityList) {
		MongoCollection<City> tableCities = mongoDatabase.getCollection(City.CityConstants.CITY, City.class);
		tableCities.drop();
		tableCities.insertMany(cityList);
	}

	/**
	 * @brief Creates the first map grouped by State.
	 * 
	 */
	public Map<String, List<City>> queryCitiesAlphabetically() throws NullPointerException {
		MongoCollection<City> tableCities = mongoDatabase.getCollection(City.CityConstants.CITY, City.class);
		AggregateIterable<City> output = tableCities.aggregate(Arrays.asList(Aggregates.sort(new Document(City.CityConstants.CITY,1)),Aggregates.sort(new Document(City.CityConstants.STATE,1))));
		Stream<City> stream = MongoDbUtil.iterableToStream(output);

		return stream.collect(Collectors.groupingBy(City::getState));
	}
}