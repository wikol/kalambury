package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.DisplayLoginEvent;
import pl.uj.edu.tcs.kalambury_maven.event.DisplayMainWindowEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.LoginUnsuccessfulEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Model;

public class AppView implements View {
	private EventReactor reactor = new EventReactor();
	private Controller controller;
	private Model model;
	private LoginWindow loginWindow;
	public AppView() {
		reactor.setHandler(DisplayLoginEvent.class, new EventHandler() {

			@Override
			public void handle(Event e) {
				AppView.this.displayLogin();
				
			}
			
		});
		reactor.setHandler(DisplayMainWindowEvent.class, new EventHandler() {
			
			@Override
			public void handle(Event e) {
				AppView.this.displayMain();
			}
		});
	}
	private void displayLogin() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					loginWindow = new LoginWindow(AppView.this);
					loginWindow.setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reactor.setHandler(LoginUnsuccessfulEvent.class, loginWindow);
	}
	private void displayMain() {
		loginWindow.dispose();
	}
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

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public Model getModel() {
		return model;
	}

}
