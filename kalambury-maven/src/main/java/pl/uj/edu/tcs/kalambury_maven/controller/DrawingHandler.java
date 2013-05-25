package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;

public class DrawingHandler implements EventHandler {

	private final AppController controller;
	
	public DrawingHandler(AppController controller){
		this.controller = controller;
	}
	
	@Override
	public void handle(Event e) {
		controller.getDrawingController().reactTo(e);
	}

}
