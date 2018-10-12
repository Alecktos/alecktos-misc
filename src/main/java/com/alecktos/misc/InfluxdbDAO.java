package com.alecktos.misc;

import com.alecktos.misc.logger.Logger;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
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

	public void writeInflux(double price, DateTime dateTime, String dbName) {
		InfluxDB influxDB = null;
		try {
			influxDB = InfluxDBFactory.connect("http://localhost:8086");
			//final String dbName = "stocks";
			influxDB.createDatabase(dbName);
			influxDB.setDatabase(dbName);

			influxDB.write(Point.measurement("disney_stock")
					.time(dateTime.toTimeStamp(), TimeUnit.MILLISECONDS)
					.field("stock_value", price)
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
