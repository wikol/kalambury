package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginUnsuccessfulEvent;
import pl.uj.edu.tcs.kalambury_maven.network.TestNetwork;
import pl.uj.edu.tcs.kalambury_maven.network.UnableToConnectException;

public class LoginAttemptHandler implements EventHandler {

	AppController controller;

	LoginAttemptHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		LoginAttemptEvent attempt = (LoginAttemptEvent) e;
		try {
			controller.setNetwork(new TestNetwork(attempt.getServer(), attempt
					.getPort(), controller));
		} catch (UnableToConnectException un) {
			controller.getView().reactTo(new LoginUnsuccessfulEvent(
					"Unable to connect to the server"));
			return;
		}
		controller.getNetwork().sendToServer(e);
	}

}
