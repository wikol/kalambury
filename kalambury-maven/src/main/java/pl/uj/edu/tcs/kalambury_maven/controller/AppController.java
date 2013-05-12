package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.view.View;

public class AppController implements Controller {
	private EventReactor reactor = new EventReactor();
	private View view;
	private Model model;
	@Override
	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	@Override
	public void setView(View v) {
		v.setController(this);
		this.view = v;
	}

	@Override
	public void setModel(Model m) {
		this.model = m;
		m.setController(this);
	}

}
