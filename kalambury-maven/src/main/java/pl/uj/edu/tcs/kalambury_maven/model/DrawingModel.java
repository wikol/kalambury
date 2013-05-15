package pl.uj.edu.tcs.kalambury_maven.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.view.View;

/**
 * Prosty model rysunku
 * @author Katarzyna Janocha, Michał Piekarz
 *
 */

public class DrawingModel implements Model {

	private Controller controller;
	private EventReactor reactor = new EventReactor();
	private List<Point> drawing = new LinkedList<>();
	private final Brush brush = new Brush(Brush.MEDIUM,Color.BLACK);
	private List<View> views = new LinkedList<>();
	
	@Override
	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	@Override
	public void setController(Controller c) {
		this.controller = c;

	}

	@Override
	public void registerView(View v) {
		this.views.add(v);
	}

	public void actualiseDrawing(List<Point> newPoints) {
		synchronized(drawing){
			drawing.addAll(newPoints);
		}
	}
	
	public List<Point> getDrawing(){
		synchronized(drawing){
			return new LinkedList<>(drawing);
		}
	}
	
	public void addHandler(Class<? extends Event> e, EventHandler h){
		reactor.setHandler(e, h);
	}
	
	public Brush getBrush(){
		synchronized(brush){
			return brush;
		}
	}
	
	public void setBrush(int radius, Color color){
		synchronized(brush){
			brush.radius = radius;
			brush.color = color;
		}
	}

}
