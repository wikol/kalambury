package pl.uj.edu.tcs.kalambury_maven.network;

import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginResponseEvent;

public class TestNetwork implements Network {

	Controller controller;
	public TestNetwork(String server, String port, Controller c) throws UnableToConnectException {
		try {
			Integer.parseInt(port);
		} catch(NumberFormatException ex) {
			throw new UnableToConnectException();
		}
		if(server.equals("bad server"))
			throw new UnableToConnectException();
		controller = c;
	}

	@Override
	public void sendToServer(Event e) {
		if(e instanceof LoginAttemptEvent && ((LoginAttemptEvent) e).getLogin().equals("bad login"))
			controller.reactTo(new LoginResponseEvent((LoginAttemptEvent)e,false));
		else
			controller.reactTo(new LoginResponseEvent((LoginAttemptEvent)e,true));
	}
}
