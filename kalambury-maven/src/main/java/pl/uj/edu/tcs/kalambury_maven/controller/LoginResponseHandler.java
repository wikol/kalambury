package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.DisplayMainWindowEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.LoginResponseEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginUnsuccessfulEvent;

public class LoginResponseHandler implements EventHandler {

	AppController controller;

	public LoginResponseHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		LoginResponseEvent response = (LoginResponseEvent) e;
		if (!response.getResponse())
			controller.getView().reactTo(
					new LoginUnsuccessfulEvent(
							"Nickname already taken. Try a different one."));
		else
			controller.getView().displayMain();
	}

}
