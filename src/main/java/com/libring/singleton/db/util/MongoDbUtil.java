package com.libring.singleton.db.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import java.util.stream.Stream;

public class MongoDbUtil {

	public static final String DB_HOST = "db_host";
	public static final String DB_PORT = "db_port";
	public static final String DB_NAME = "db_name";

	public static <T> Stream<T> iterableToStream(Iterable<T> iterable) {
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(
						iterable.iterator(),
						Spliterator.IMMUTABLE
						),
						false
				);
	}

	public static Properties getMongoDbProperties() throws IOException {
		Properties properties = new Properties();
		InputStream input = MongoDbUtil.class.getResourceAsStream("/mongodb.properties");
		properties.load(input);
		return properties;
	}
}