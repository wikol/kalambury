package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.PointsChangedEvent;

public class PointsChangedHandler implements EventHandler{
	AppController controller;
	
	public PointsChangedHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		PointsChangedEvent castedEvent = (PointsChangedEvent) e;
		controller.getModel().getUserRanking().setPointsForUser(castedEvent.getUser(), castedEvent.getNewPointsNumber());
		
	}

}
