package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.LinkedList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.model.Point;

/**
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 *
 */
public class NewPointsDrawnEvent implements Event {
	
	private List<Point> newPoints;

	public NewPointsDrawnEvent(List<Point> newPoints){
		this.newPoints = new LinkedList<>(newPoints);
	}
	
	public List<Point> getPoints(){
		return new LinkedList<>(newPoints);
	}
}
