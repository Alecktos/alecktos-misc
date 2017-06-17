package com.alecktos.misc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LineFileReaderTest {

	private static String disneyStockTestPath = "src/test/disneyStockTest.txt";

	private static void removeTestFile() {
		try {
			Files.deleteIfExists(FileSystems.getDefault().getPath(disneyStockTestPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeClass
	public static void cleanUpBeforeAllTest() {
		removeTestFile();
	}

	@AfterClass
	public static void cleanUpAfterAllTestsRun() {
		removeTestFile();
	}

	@Test
	public void testStuff() {

		final String firstLine = "content-interval:5m  open:14.30-21.00 #time is in UTC";
		final String secondLine = "98.27:1459175404887";

		try {
			PrintWriter printWriter = FileHandler.getFileWriter(disneyStockTestPath);

			printWriter.println(firstLine);
			printWriter.println(secondLine);
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		LineFileReader lineFileReader = new LineFileReader();
		final List<String> linesFromFile = lineFileReader.getLinesFromFile(disneyStockTestPath, -1);

		assertEquals(firstLine, linesFromFile.get(0));
		assertEquals(secondLine, linesFromFile.get(1));

		try {
			FileHandler.deleteFile(disneyStockTestPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
