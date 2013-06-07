package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.RiddleEvent;

public class RiddleHandler implements EventHandler{
	private AppController controller;
	
	public RiddleHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		RiddleEvent riddleEvent = (RiddleEvent) e;
		controller.getView().getMainWindow().getTimer().setRiddle(riddleEvent.getRiddle());
	}
}
