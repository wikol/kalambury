package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;

public class UsersOfflineHandler implements EventHandler {

	AppController controller;
	public UsersOfflineHandler(AppController controller) {
		this.controller = controller;
	}
	@Override
	public void handle(Event e) {
		UsersOfflineEvent event = (UsersOfflineEvent) e;
		controller.getModel().getChatMessagesList().reactTo(e);
		for(String name : event.getUsersOffline())
			controller.getModel().getUserRanking().deleteUser(name);
	}

}
