package com.infinite;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**
 * Silly example of how to connect to MongoDB from Java.
 * 
 * JavaDoc can be found at http://api.mongodb.org/java/2.10.1/
 * 
 * To run this application, right click, select "Run As" -> "Java Application" 
 *
 */
public class TheSimplestExample {

	private static final String HOST = "localhost";
	private static final String DATABASE_NAME = "inifinite-drawing-test";
	private static final String COLLECTION_NAME = "a-collection";

	private DBCollection collection;

	private TheSimplestExample() {
		System.out.println("Creating connection to " + HOST
				+ "\" + DATABASE_NAME");
		try {
			MongoClient mongoClient = new MongoClient(HOST);
			DB db = mongoClient.getDB(DATABASE_NAME);
			this.collection = db.getCollection(COLLECTION_NAME);
		} catch (Exception e) {
			System.err.println("Cannot connect to " + HOST
					+ " - is it running?");
			System.exit(1);
		}
	}

	private void createSomeData() {
		for (int i = 0; i < 100; i++) {
			for (int x = 0; i < 100; i++) {
				// I wonder what this will look like :)
				BasicDBObject line = new BasicDBObject();
				line.put("start", coord(i, x));
				line.put("end", coord(x, i));
				this.collection.save(line, WriteConcern.ACKNOWLEDGED); // read
																		// about
																		// WriteConcerns
																		// -
																		// they
																		// impact
																		// performance
			}
		}
	}

	private void retrieveSomeData() {
		DBCursor lines = this.collection.find(); // get them all
		for (DBObject line : lines) {
			DBObject start = (DBObject) line.get("start");
			DBObject end = (DBObject) line.get("end");
			System.out.println(String.format("%s to %s", formatCoord(start),
					formatCoord(end)));
		}
	}

	private BasicDBObject coord(int x, int y) {
		BasicDBObject o = new BasicDBObject();
		o.put("x", x);
		o.put("y", y);
		return o;
	}

	private String formatCoord(DBObject coord) {
		return String.format("x:%s, y:%s", coord.get("x"), coord.get("y"));
	}

	public static void main(String[] args) {
		TheSimplestExample tom = new TheSimplestExample();
		tom.createSomeData();
		tom.retrieveSomeData();
	}
}
