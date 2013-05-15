package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.List;
import java.util.ArrayList;

import pl.uj.edu.tcs.kalambury_maven.model.Point;

/**
 * Event informujący o aktualizacji rysunku.
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 *
 */

public class DrawingActualisationEvent implements Event {
	
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 6026599969967575937L;
	
	/**
	 * Lista punktów, w których zaszła zmiana (punktów do aktualizacji)
	 */
	
	public final List <Point> newPoints = new ArrayList<Point>();
	public String destination;

	/**
	 * Konstruktor
	 * @param newPoints - zbiór punktów, dla których zaszła zmiana
	 */
	public DrawingActualisationEvent(List <Point> newPoints) {
		this.newPoints.addAll(newPoints);
		destination = "CONTROLLER";
	}
}
