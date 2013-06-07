package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;

public class NextRoundStartsHandler implements EventHandler {
	private AppController controller;
	
	public NextRoundStartsHandler(AppController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(Event e) {
		NextRoundStartsEvent castedEvent = (NextRoundStartsEvent) e;
		controller.getModel().getUserRanking().nextRound(castedEvent.getDrawingUser());
		controller.getView().getMainWindow().getTimer().setRoundTime(castedEvent.getRoundTime()/1000);
		controller.getView().getMainWindow().getTimer().startNextRound(castedEvent.getTimeLeft()/1000);
		controller.getView().getMainWindow().getTimer().setRiddle("???");
	}

}
