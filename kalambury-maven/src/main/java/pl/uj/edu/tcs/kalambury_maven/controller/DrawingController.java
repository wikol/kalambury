package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.DrawingActualisationEvent;
import pl.uj.edu.tcs.kalambury_maven.event.DrawingActualisationEventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.view.DrawingPanel;
import pl.uj.edu.tcs.kalambury_maven.view.View;

public class DrawingController implements Controller {

	private final EventReactor reactor = new EventReactor();
	private DrawingPanel view;
	private DrawingModel model;
	
	public void addHandler(Class<? extends Event> e, EventHandler h){
		reactor.setHandler(e, h);
	}
	
	@Override
	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	@Override
	public void setView(View v) {
		v.setController(this);
		if (v instanceof DrawingPanel)
			this.view = (DrawingPanel)v;
	}

	@Override
	public void setModel(Model m) {
		if (m instanceof DrawingModel)
			this.model = (DrawingModel)m;
		m.setController(this);
	}

}
