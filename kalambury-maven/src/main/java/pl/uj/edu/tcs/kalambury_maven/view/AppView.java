package pl.uj.edu.tcs.kalambury_maven.view;

import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.model.Model;

public class AppView implements View {
	private EventReactor reactor = new EventReactor();
	private Controller controller;
	private Model model;
	@Override
	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	@Override
	public void setController(Controller c) {
		this.controller = c;
	}

	@Override
	public void setModel(Model m) {
		this.model = m;
		m.registerView(this);
	}

}
