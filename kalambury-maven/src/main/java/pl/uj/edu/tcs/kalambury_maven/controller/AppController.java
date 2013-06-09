package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.BrushChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ClearScreenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.LoginAttemptEvent;
import pl.uj.edu.tcs.kalambury_maven.event.LoginResponseEvent;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordForGuessingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordIsNeededEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;
import pl.uj.edu.tcs.kalambury_maven.event.PointsChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.CloseWordInputEvent;
import pl.uj.edu.tcs.kalambury_maven.event.RiddleEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;
import pl.uj.edu.tcs.kalambury_maven.network.Network;
import pl.uj.edu.tcs.kalambury_maven.view.AppView;

public class AppController {
	private EventReactor reactor = new EventReactor();
	private Network network;
	private AppView view;
	private SimpleModel model;
	private DrawingController drawingController;

	public AppController() {
		reactor.setHandler(BrushChangedEvent.class, new DrawingHandler(this));
		reactor.setHandler(ClearScreenEvent.class, new DrawingHandler(this));
		reactor.setHandler(NewPointsDrawnEvent.class, new DrawingHandler(this));
		setView(new AppView());
		setModel(new SimpleModel());
		view.setModel(model);
		reactor.setHandler(NewMessageWrittenEvent.class, new NewMessageWrittenHandler(this));
		reactor.setHandler(MessageSendEvent.class, new MessageSendHandler(this));
		reactor.setHandler(LoginAttemptEvent.class, new LoginAttemptHandler(
				this));
		reactor.setHandler(LoginResponseEvent.class, new LoginResponseHandler(
				this));
		reactor.setHandler(UsersOnlineEvent.class, new UsersOnlineHandler(this));
		reactor.setHandler(UsersOfflineEvent.class, new UsersOfflineHandler(
				this));
		reactor.setHandler(WordGuessedEvent.class, new WordGuessedHandler(this));
		reactor.setHandler(NewWordForGuessingEvent.class, new NewWordForGuessingHandler(this));
		reactor.setHandler(NewWordIsNeededEvent.class, new NewWordIsNeededHandler(this));
		reactor.setHandler(CloseWordInputEvent.class, new CloseWordInputHandler(this));
		reactor.setHandler(NextRoundStartsEvent.class, new NextRoundStartsHandler(this));
		reactor.setHandler(ResetUserRankingEvent.class, new ResetUserRankingHandler(this));
		reactor.setHandler(PointsChangedEvent.class, new PointsChangedHandler(this));
		reactor.setHandler(RiddleEvent.class, new RiddleHandler(this));	
		view.displayChoice();
		drawingController = new DrawingController();
		drawingController.setModel(model.getDrawingModel());
	}

	public DrawingController getDrawingController() {
		return drawingController;
	}

	public synchronized void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}

	public void setView(AppView v) {
		v.setController(this);
		this.view = v;
	}

	public void setModel(SimpleModel m) {
		this.model = m;
		//m.setController(this);
	}

	public void setNetwork(Network network) {
		this.network = network;
		drawingController.setNetwork(network);
	}

	public Network getNetwork() {
		return network;
	}

	public AppView getView() {
		return view;
	}

	public SimpleModel getModel() {
		return model;
	}

	/**
	 * For testing purposes only
	 */
	public void testMainWindow() {
		/*List<String> names = Arrays.asList(new String[] { "Michał Glapa",
				"Karol Kaszuba", "Kamil Rychlewicz", "Piotr Pakosz" });
		reactTo(new UsersOnlineEvent(names));
		List<String> pakosz = Arrays.asList(new String[] { "Piotr Pakosz" });
		for(int i = 0;i<350;i++)
			reactTo(new WordGuessedEvent("Laser", "Karol Kaszuba"));
		for(int i = 0;i<280;i++)
			reactTo(new WordGuessedEvent("Laser", "Michał Glapa"));
		for(int i = 0;i<370;i++)
			reactTo(new WordGuessedEvent("Laser", "Kamil Rychlewicz"));
		reactTo(new UsersOfflineEvent(pakosz));*/
	}

	/**
	 * For test purposes only
	 */
	public static void main(String[] args) {
		//RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
		new AppController();
	}
}
