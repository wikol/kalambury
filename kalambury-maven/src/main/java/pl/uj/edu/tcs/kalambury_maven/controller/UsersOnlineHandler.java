package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;

public class UsersOnlineHandler implements EventHandler {

	AppController controller;
	public UsersOnlineHandler(AppController controller) {
		this.controller = controller;
	}
	@Override
	public void handle(Event e) {
		UsersOnlineEvent event = (UsersOnlineEvent) e;
		controller.getModel().getChatMessagesList().reactTo(e);
		for(String name : event.getUsersOnline())
			controller.getModel().getUserRanking().addNewUser(name);
	}

}
