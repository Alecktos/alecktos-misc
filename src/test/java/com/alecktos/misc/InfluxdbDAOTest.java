package com.alecktos.misc;

import org.influxdb.dto.Point;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class InfluxdbDAOTest {

	private final static String dbName = "db_test";

	@Before
	public void cleanUp() {
		InfluxdbDAO.deleteDb(dbName);
	}

	@Test
	public void testInsertingValue() {
		final double price = 2.0;
		final InfluxdbDAO influxdbDAO = new InfluxdbDAO();
		final String measurements = "disney_stock";

		Map<String, Object> fieldsToAdd = new HashMap<>();
		fieldsToAdd.put("stock_value", price);

		Point point = InfluxdbDAO.createPoint(measurements, DateTime.createFromNow(), fieldsToAdd);

		influxdbDAO.writeInflux(asList(point), dbName);

		try {
			String query = "select * from disney_stock";
			String q = URLEncoder.encode(query, "UTF-8");
			final String result = influxdbDAO.executeGet("http://localhost:8086/query?db=db_test&pretty=false&q=" + q);

			JSONObject jsonObject = new JSONObject(result);
			final JSONArray values =
					jsonObject
							.getJSONArray("results")
							.getJSONObject(0)
							.getJSONArray("series")
							.getJSONObject(0)
							.getJSONArray("values");

			//fixa detta testet
			assertEquals(1, values.length());
			assertEquals(price, values.getJSONArray(0).getDouble(1), 0.001);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
