package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;

public class WordGuessedHandler implements EventHandler {

	AppController controller;

	public WordGuessedHandler(AppController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(Event e) {
		WordGuessedEvent event = (WordGuessedEvent) e;
		controller.getModel().getChatMessagesList().reactTo(e);
	}

}
