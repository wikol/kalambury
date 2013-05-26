package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;

public class NewWordForGuessingHandler implements EventHandler {
	AppController controller;

	public NewWordForGuessingHandler(AppController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(Event e) {
		controller.getNetwork().sendToServer(e);

	}

}
