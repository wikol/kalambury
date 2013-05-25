package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.StartDrawingEvent;

public class StartDrawingHandler implements EventHandler {
	AppController controller;
	
	public StartDrawingHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		controller.getView().closeWordInput();
	}

}
