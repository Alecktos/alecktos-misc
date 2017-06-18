package com.alecktos.misc.logger;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EmailNotifierTest {

	@Test
	public void createEmailNotifierTest() {

		Map<String, String> emailValues = new EmailNotifier("src/test/resources/email_test.yml") {

			private Map<String, String> getEmailValues() {
				Map<String, String> emailValues = new HashMap<>();
				emailValues.put("fromAddress", this.fromAddress);
				emailValues.put("receiverAddress", this.receiverAddress);
				emailValues.put("hostName", this.hostName);
				emailValues.put("userName", this.userName);
				emailValues.put("password", this.password);
				return emailValues;
			}

		}.getEmailValues();

		assertEquals("a.dse@yahoo.com", emailValues.get("fromAddress"));
		assertEquals("fhdsjk@gmail.com", emailValues.get("receiverAddress"));
		assertEquals("smtp.mail.gmail.com", emailValues.get("hostName"));
		assertEquals("stuff.ae@yahoo.com", emailValues.get("userName"));
		assertEquals("fhdjsk12!", emailValues.get("password"));
	}
}
