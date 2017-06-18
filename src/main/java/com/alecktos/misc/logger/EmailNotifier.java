package com.alecktos.misc.logger;

import com.google.inject.name.Named;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.Authenticator;
import java.io.Serializable;

public class EmailNotifier implements AlertNotifierInterface, Serializable {

	private final String receiverAddress;
	private final String hostName;
	private final String sentFrom;
	private final Authenticator defaultAuthenticator;

	public EmailNotifier(@Named("emailReceiverAddress") final String emailReceiverAddress, @Named("emailHostName") final String emailHostName,
	                     @Named("emailSentFrom") final String emailSentFrom, final Authenticator authenticator) {
		this.receiverAddress = emailReceiverAddress;
		this.hostName = emailHostName;
		this.sentFrom = emailSentFrom;
		this.defaultAuthenticator = authenticator;
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
