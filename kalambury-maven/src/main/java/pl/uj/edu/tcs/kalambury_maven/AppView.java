package pl.uj.edu.tcs.kalambury_maven;

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
