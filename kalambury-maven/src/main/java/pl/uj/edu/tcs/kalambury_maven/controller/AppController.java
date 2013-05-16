package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.DisplayLoginEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginResponseEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;
import pl.uj.edu.tcs.kalambury_maven.network.Network;
import pl.uj.edu.tcs.kalambury_maven.view.AppView;
import pl.uj.edu.tcs.kalambury_maven.view.View;

public class AppController implements Controller {
	private EventReactor reactor = new EventReactor();
	private Network network;
	private View view;
	private Model model;

	public AppController() {
		setView(new AppView());
		setModel(new SimpleModel());
		view.setModel(model);
		reactor.setHandler(LoginAttemptEvent.class, new LoginAttemptHandler(
				this));
		reactor.setHandler(LoginResponseEvent.class, new LoginResponseHandler(
				this));
		view.reactTo(new DisplayLoginEvent());
	}

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

	@Override
	public void setNetwork(Network network) {
		this.network = network;
	}

	@Override
	public Network getNetwork() {
		return network;
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public Model getModel() {
		return model;
	}

	/**
	 * For test purposes only
	 */
	public static void main(String[] args) {
		AppController program = new AppController();
	}
}
