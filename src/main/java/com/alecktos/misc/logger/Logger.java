package com.alecktos.misc.logger;

import com.alecktos.misc.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class Logger implements Serializable {

	private List<Class> ignoreClasses = new ArrayList<>();
	private String subject = null;
	private boolean alert = false;

	private AlertNotifierInterface alertNotifier;

	@Inject
	public Logger(final AlertNotifierInterface alertNotifier) {
		this.alertNotifier = alertNotifier;
	}

	public void setIgnore(Class[] excludedClasses) {
		ignoreClasses = new ArrayList<>(Arrays.asList(excludedClasses));
	}

	public void log(String info, Class callingClass) {
		if (ignoreClasses.contains(callingClass)) {
			return;
		}

		String formattedInfo = getFormattedInfo(info, callingClass.getSimpleName());
		System.out.println(formattedInfo);
	}

	public void logAndAlert(String info, Class callingClass) {
		log(info, callingClass);
		if(alert) {
			alertNotifier.notify(getFormattedInfo(info, callingClass.getSimpleName()), subject);
		}
	}

	private String getFormattedInfo(String info, String simpleName) {
		String utcTime = DateTime.createFromNow().toString();
		return "Current UTC Time: " + utcTime + ": " + info + " [Class: " + simpleName + "]";
	}
}