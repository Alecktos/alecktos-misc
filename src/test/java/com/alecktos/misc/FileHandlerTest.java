package com.alecktos.misc;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileHandlerTest {

	@Test
	public void testSavingLoadingObject() {
		try {
			String testFilepath = "test.data";

			FileHandler.deleteFile(testFilepath);

			SerializableMock sm = new SerializableMock();
			sm.value = 42;
			FileHandler.writeObjectToDisk(sm, testFilepath);
			sm = (SerializableMock) FileHandler.readObjectFromDisk(testFilepath);
			assertEquals(42, sm.value);

			FileHandler.deleteFile(testFilepath);
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
