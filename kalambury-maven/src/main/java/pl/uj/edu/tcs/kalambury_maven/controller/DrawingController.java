package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.BrushChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
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

	/**
	 * Aktualizuje rysunek przechowywany w modelu
	 * 
	 * @param newPoints
	 *            - nowo narysowane punkty
	 */
	public void actualiseDrawing(Event e) {
		NewPointsDrawnEvent realEvent = (NewPointsDrawnEvent) e;
		model.actualiseDrawing(realEvent.getPoints());
	}

	/**
	 * Wysyła zdarzenie na serwer
	 * 
	 * @param e
	 */
	public void sendEventToServer(Event e) {
		network.sendToServer(e);
	}

	/**
	 * Aktualizuje pędzel przechowywany w modelu
	 * 
	 * @param radius
	 *            - promień nowego pędzla
	 * @param color
	 *            - kolor nowego pędzla
	 */
	public void actualiseBrush(Event e) {
		BrushChangedEvent realEvent = (BrushChangedEvent) e;
		model.setBrush(realEvent.getBrush().radius, realEvent.getBrush().color);
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
}
