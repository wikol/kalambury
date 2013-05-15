package pl.uj.edu.tcs.kalambury_maven.event;

import java.awt.Color;

import pl.uj.edu.tcs.kalambury_maven.model.Brush;

public class BrushChangedEvent implements Event {
	
	public int radius;
	public Color color;
	public String destination;
	
	public BrushChangedEvent(int radius, Color color){
		this.radius = radius;
		this.color = color;
		this.destination = "CONTROLLER";
	}
	
	public BrushChangedEvent(Brush brush){
		this.radius = brush.radius;
		this.color = brush.color;
		this.destination = "VIEW";
	}
}
