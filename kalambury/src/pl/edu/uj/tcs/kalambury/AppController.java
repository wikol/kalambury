package pl.edu.uj.tcs.kalambury;

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
