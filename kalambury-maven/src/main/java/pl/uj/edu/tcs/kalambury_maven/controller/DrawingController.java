package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.BrushChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.network.Network;

/**
 * Controller do DrawingPanel
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 * 
 */
public class DrawingController {

	private final EventReactor reactor = new EventReactor();
	private DrawingModel model;
	private Network network;

	public DrawingController() {
		reactor.setHandler(NewPointsDrawnEvent.class, new EventHandler() {

			@Override
			public void handle(Event e) {
				NewPointsDrawnEvent realEvent = (NewPointsDrawnEvent) e;
				model.actualiseDrawing(realEvent.getPoints());
			}

		});

		reactor.setHandler(BrushChangedEvent.class, new EventHandler() {

			@Override
			public void handle(Event e) {
				BrushChangedEvent realEvent = (BrushChangedEvent) e;
				model.setBrush(realEvent.getBrush().radius,
						realEvent.getBrush().color);
			}

		});
	}

	/**
	 * Wysyła zdarzenie na serwer
	 * 
	 * @param e
	 */
	public void sendEventToServer(Event e) {
		this.reactTo(e);
	}

	/**
	 * Ustawia model na którym ma operaować controller
	 * 
	 * @param model
	 */
	public void setModel(DrawingModel model) {
		this.model = model;
	}

	/**
	 * Ustawia klasę do łączenia się z serwerem
	 * 
	 * @param network
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * Reakcja na wydarzenie z serwera
	 */
	public void reactTo(Event e) {
		try {
			reactor.handle(e);
		} catch (EventNotHandledException e1) {
			e1.printStackTrace();
		}
	}
}
