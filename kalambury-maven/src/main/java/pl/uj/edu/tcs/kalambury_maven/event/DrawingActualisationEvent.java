package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.List;
import java.util.ArrayList;

import pl.uj.edu.tcs.kalambury_maven.model.Point;

/**
 * Event informujący o aktualizacji rysunku.
 * 
 * @author Katarzyna Janocha
 *
 */

public class DrawingActualisationEvent implements Event {
	
	List <Point> newPoints = new ArrayList<Point>();
	
	public DrawingActualisationEvent(List <Point> newPoints) {
		this.newPoints = newPoints;
	}
}
