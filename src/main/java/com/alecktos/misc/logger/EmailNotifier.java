package com.alecktos.misc.logger;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Map;

public class EmailNotifier implements AlertNotifierInterface, Serializable {

	protected String receiverAddress;
	protected String fromAddress;
	protected String hostName;
	protected String password;
	protected String userName;

	@Inject
	public EmailNotifier(@Named("emailConfigPath") final String emailConfigPath) {
		YamlReader yamlReader = null;
		try {
			yamlReader = new YamlReader(new FileReader(emailConfigPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Map email = null;
		try {
			email = (Map) ((Map)yamlReader.read()).get("email");
		} catch (YamlException e) {
			e.printStackTrace();
		}



		receiverAddress = (String) email.get("receiveraddress");
		fromAddress = (String) email.get("fromaddress");
		hostName = (String) email.get("hostname");

		Map authentication = (Map) email.get("authentication");
		userName = (String) authentication.get("username");
		password = (String) authentication.get("password");
	}

	public void notify(String message, String subject) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(hostName);
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator(this.userName, this.password));
			email.setSSLOnConnect(true);
			email.setFrom(fromAddress);
			email.setSubject(subject);

			email.setTextMsg(message);
			email.addTo(receiverAddress);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

}
