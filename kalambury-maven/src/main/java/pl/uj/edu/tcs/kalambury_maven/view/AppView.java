package pl.uj.edu.tcs.kalambury_maven.view;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.controller.Controller;
import pl.uj.edu.tcs.kalambury_maven.event.DisplayLoginEvent;
import pl.uj.edu.tcs.kalambury_maven.event.DisplayMainWindowEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.LoginUnsuccessfulEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;

public class AppView {
	private EventReactor reactor = new EventReactor();
	private AppController controller;
	private SimpleModel model;
	private LoginWindow loginWindow;
	private MainWindow mainWindow;
	private WordInputWindow wordInputWindow;

	public AppView() {
		reactor.setHandler(DisplayLoginEvent.class, new EventHandler() {

			@Override
			public void handle(Event e) {
				AppView.this.displayLogin();

			}

		});
	}

	public void displayLogin() {
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

	public void displayMain(String myNick) {
		final String nick = myNick;
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					loginWindow.dispose();
					mainWindow = new MainWindow(nick);
					mainWindow.setupChatBox(model.getChatMessagesList(),
							controller);
					mainWindow.setupDrawingPanel(
							controller.getDrawingController(),
							model.getDrawingModel());
					model.registerChatBox(AppView.this);
					model.registerRanking(AppView.this);
					model.registerDrawingPanel(AppView.this);
					mainWindow.setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.getCause().printStackTrace();
		}
		// for testing purposes only
		controller.testMainWindow();
	}
	
	public void displayWordInput() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					wordInputWindow = new WordInputWindow();
					wordInputWindow.setController(controller);
					wordInputWindow.setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.getCause().printStackTrace();
		}
	}
	
	public void closeWordInput() {
		wordInputWindow.dispose();
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	public void setController(AppController c) {
		this.controller = c;
	}

	public void setModel(SimpleModel m) {
		this.model = m;
	}

	public AppController getController() {
		return controller;
	}

	public SimpleModel getModel() {
		return model;
	}

}
