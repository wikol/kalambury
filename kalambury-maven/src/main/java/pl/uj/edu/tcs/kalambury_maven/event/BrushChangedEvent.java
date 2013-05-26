package pl.uj.edu.tcs.kalambury_maven.event;

import java.awt.Color;

import pl.uj.edu.tcs.kalambury_maven.model.Brush;

/**
 * 
 * @author Katarzyna Janocha, Michał Piekarz
 *
 */
public class BrushChangedEvent implements Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8104298489869998055L;
	private Color color;
	private int radius;

	public BrushChangedEvent(int radius, Color color){
		this.radius = radius;
		this.color = color;
	}
	
	public Brush getBrush(){
		return new Brush(radius,color);
	}
	
}
