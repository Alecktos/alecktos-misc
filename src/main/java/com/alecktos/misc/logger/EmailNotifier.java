package com.alecktos.misc.logger;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.Serializable;

public class EmailNotifier implements AlertNotifierInterface, Serializable {

	private final String receiverAddress;
	private final String hostName;
	private final String sentFrom;
	private final DefaultAuthenticator defaultAuthenticator;

	public EmailNotifier(String receiverAddress, String hostName, String sentFrom, DefaultAuthenticator defaultAuthenticator) {
		this.receiverAddress = receiverAddress;
		this.hostName = hostName;

		this.sentFrom = sentFrom;
		this.defaultAuthenticator = defaultAuthenticator;
	}

	public void notify(String message, String subject) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(hostName);
			email.setSmtpPort(465);
			email.setAuthenticator(defaultAuthenticator);
			email.setSSLOnConnect(true);
			email.setFrom(sentFrom);
			email.setSubject(subject);

			email.setTextMsg(message);
			email.addTo(receiverAddress);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

}
