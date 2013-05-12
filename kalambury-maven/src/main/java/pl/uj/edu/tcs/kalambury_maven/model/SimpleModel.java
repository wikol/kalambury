package pl.uj.edu.tcs.kalambury_maven.model;

import java.util.ArrayList;
import java.util.Collection;

import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.view.View;

public class SimpleModel implements Model{
	private EventReactor reactor = new EventReactor();
	private Controller controller;
	private Collection<View> views = new ArrayList<View> ();
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
		views.add(v);
	}

}
