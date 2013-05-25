package pl.uj.edu.tcs.kalambury_maven.controller;

import java.awt.EventQueue;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordIsNeededEvent;
import pl.uj.edu.tcs.kalambury_maven.view.WordInputWindow;

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
