package com.alecktos.misc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineFileReader {

	public List<String> getLinesFromFile(final String filePath) throws RuntimeException {
		return getLinesFromFile(filePath, -1);
	}

	/**
	 *
	 * @param filePath
	 * @param numberOfLines (-1 for all lines)
	 * @return
	 * @throws RuntimeException
	 */
	public List<String> getLinesFromFile(final String filePath, final int numberOfLines) throws RuntimeException {
		final List<String> lines;

		//By putting it in a try statement I ensure that it is always closed (java 7 and above feature).
		try (BufferedReader br = FileHandler.getFileReader(filePath)) {
			lines = getLineArrayFromFile(br, numberOfLines);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find file to read from: " + e.getMessage() + e.getStackTrace().toString());
		} catch (IOException e) {
			throw new RuntimeException("Failed reading from file: " + e.getMessage() + e.getStackTrace().toString());
		}

		return lines;
	}

	public String getLastLine(final String filePath) throws RuntimeException {
		final List<String> linesFromFile = getLinesFromFile(filePath, -1);
		int lastIndex = linesFromFile.size() - 1;
		return linesFromFile.get(lastIndex);
	}

	private List<String> getLineArrayFromFile(BufferedReader br, int numberOfLines) throws IOException {
		int numberOfLinesFetched = 0;
		if(numberOfLines < 0) {
			numberOfLines = Integer.MAX_VALUE;
		}

		final List<String> lines = new ArrayList<>();
		String line = br.readLine();
		while(line != null) {
			lines.add(line);
			line = br.readLine();
			numberOfLinesFetched++;
			if(numberOfLinesFetched >= numberOfLines) {
				break;
			}
		}

		return lines;
	}
}
