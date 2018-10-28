package com.alecktos.misc;

import com.alecktos.misc.logger.Logger;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

	public void writeInflux(Map<String, Object> fieldsToAdd, DateTime dateTime, String measurement, String dbName) {
		InfluxDB influxDB = null;
		try {
			influxDB = InfluxDBFactory.connect("http://localhost:8086");

			// Flush every 4000 Points, at least every 200ms
			influxDB.enableBatch(BatchOptions.DEFAULTS.actions(4000).flushDuration(200));

			influxDB.createDatabase(dbName);
			influxDB.setDatabase(dbName);

			influxDB.write(Point.measurement(measurement)
					.time(dateTime.toTimeStamp(), TimeUnit.MILLISECONDS)
					.fields(fieldsToAdd)
					.build());
			influxDB.close();

		} catch(Exception e) {
			Logger.doAlert("Nått gick fel när skrev till influx: " + e.getMessage());
		} finally {
			if(influxDB != null) {
				influxDB.close();
			}
		}
	}

	public static void deleteDb(String dbName) {
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
		influxDB.deleteDatabase(dbName);
		influxDB.close();
	}


}
