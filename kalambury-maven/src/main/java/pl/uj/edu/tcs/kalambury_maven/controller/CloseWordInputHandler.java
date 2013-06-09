package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;

public class CloseWordInputHandler implements EventHandler {
	AppController controller;
	
	public CloseWordInputHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		controller.getView().closeWordInput();
	}

}
