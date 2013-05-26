package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;

public class ResetUserRankingHandler implements EventHandler {
	AppController controller;
	
	public ResetUserRankingHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		ResetUserRankingEvent castedEvent = (ResetUserRankingEvent) e;
		controller.getModel().getUserRanking().setUsersPoints(castedEvent.getUsersPoints());
	}

}
