package pl.edu.uj.tcs.kalambury;

import java.util.ArrayList;
import java.util.Collection;

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
