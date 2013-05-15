package pl.uj.edu.tcs.kalambury_maven.event;

import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.view.DrawingPanel;
import pl.uj.edu.tcs.kalambury_maven.view.View;

/**
 * Handler do DrawingActualisationEvent
 * @author Katarzyna Janocha, Michał Piekarz
 *
 */
public class DrawingActualisationEventHandler implements EventHandler {

	private final DrawingModel model;
//	private final Server server;
	private final DrawingPanel view;
	
	public DrawingActualisationEventHandler(DrawingModel model, DrawingPanel view){
		this.model = model;
		this.view = view;
	}
	
	@Override
	public synchronized void handle(Event e) {
		// TODO zamiana na komunikację z serwerem
		DrawingActualisationEvent realEvent = (DrawingActualisationEvent)e;
		if (realEvent.destination.equals("CONTROLLER")){
			try {
				realEvent.destination = "MODEL";
				model.reactTo(realEvent);
			} catch (EventNotHandledException e1) {
				e1.printStackTrace();
			}
		} else if (realEvent.destination.equals("MODEL")){
			model.actualiseDrawing(realEvent.newPoints);
			try {
				DrawingActualisationEvent newEvent = new DrawingActualisationEvent(model.getDrawing());
				newEvent.destination = "VIEW";
				view.reactTo(newEvent);
			} catch (EventNotHandledException e1) {
				e1.printStackTrace();
			}System.out.println("OVER");
		} else if (realEvent.destination.equals("VIEW")){
			view.drawPoints(realEvent.newPoints);
		}
	}

}
