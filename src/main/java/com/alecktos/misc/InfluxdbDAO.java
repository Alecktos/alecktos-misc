package com.alecktos.misc;

import com.alecktos.misc.logger.Logger;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InfluxdbDAO {

	//Dont think anyone is using this right now
	/*public static void executePost(String endpoint, String data) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(endpoint);

		StringEntity entity = new StringEntity(data);
		httpPost.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPost);

		System.out.println("status line: " + response.getStatusLine());

		client.close();
	}*/

	public static String executeGet(String q) throws IOException {
		URL url = new URL(q);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;

		StringBuilder result = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			//System.out.println(inputLine);
			result.append(inputLine + "\n");
		}
		in.close();
		return result.toString();
	}

	public static Point createPoint(String measurement, DateTime dateTime, Map<String, Object> fields) {
		return createPoint(measurement, dateTime, fields, new HashMap<>());
	}

	public static Point createPoint(String measurement, DateTime dateTime, Map<String, Object> fields, Map<String, String> tags) {
		return Point.measurement(measurement)
				.time(dateTime.toTimeStamp(), TimeUnit.MILLISECONDS)
				.fields(fields)
				.tag(tags)
				.build();
	}

	public void writeInflux(List<Point> points, String dbName) {
		final InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");

			// Flush every 4000 Points, at least every 600ms
			influxDB.enableBatch(BatchOptions.DEFAULTS.actions(4000).flushDuration(600));

			influxDB.createDatabase(dbName);
			influxDB.setDatabase(dbName);

			//final InfluxDB finalInfluxDB = influxDB;
			points.forEach(point -> influxDB.write(point));
//			influxDB.write(Point.measurement(measurement)
//					.time(dateTime.toTimeStamp(), TimeUnit.MILLISECONDS)
//					.fields(fieldsToAdd)
//					.build());

			influxDB.close();
	}

	public static void deleteDb(String dbName) {
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
		influxDB.deleteDatabase(dbName);
		influxDB.close();
	}


}
