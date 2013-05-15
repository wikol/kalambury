package pl.uj.edu.tcs.kalambury_maven.event;

import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.view.DrawingPanel;

public class BrushChangedEventHandler implements EventHandler {

	private final DrawingModel model;
	private final DrawingPanel view;

	public BrushChangedEventHandler(DrawingModel model, DrawingPanel view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void handle(Event e) {
		// TODO zamiana na komunikację z serwerem
		BrushChangedEvent realEvent = (BrushChangedEvent) e;
		if (realEvent.destination.equals("CONTROLLER")) {
			try {
				realEvent.destination = "MODEL";
				model.reactTo(realEvent);
			} catch (EventNotHandledException e1) {
				e1.printStackTrace();
			}
		} else if (realEvent.destination.equals("MODEL")) {
			model.setBrush(realEvent.radius,realEvent.color);
			try {
				BrushChangedEvent newEvent = new BrushChangedEvent(model.getBrush());
				newEvent.destination = "VIEW";
				view.reactTo(newEvent);
			} catch (EventNotHandledException e1) {
				e1.printStackTrace();
			}
			System.out.println("OVER");
		} else if (realEvent.destination.equals("VIEW")) {
			view.setBrush(realEvent.radius,realEvent.color);
		}
	}

}
