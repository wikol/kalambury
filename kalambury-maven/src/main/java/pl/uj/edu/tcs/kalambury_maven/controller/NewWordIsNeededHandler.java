package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;

public class NewWordIsNeededHandler implements EventHandler {
	private AppController controller;
	
	public NewWordIsNeededHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		controller.getView().displayWordInput();
	}

}
