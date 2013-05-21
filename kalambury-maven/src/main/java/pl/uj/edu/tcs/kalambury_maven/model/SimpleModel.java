package pl.uj.edu.tcs.kalambury_maven.model;

import pl.uj.edu.tcs.kalambury_maven.controller.AppController;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.view.AppView;

public class SimpleModel {
	private EventReactor reactor = new EventReactor();
	private AppController controller;
	private ChatMessagesList chatMessagesList;
	private UserRanking userRanking;
	private DrawingModel drawingModel;

	public SimpleModel() {
		chatMessagesList = new ChatMessagesList();
		userRanking = new UserRanking();
		drawingModel = new DrawingModel();
	}
	
	public DrawingModel getDrawingModel() {
		return drawingModel;
	}
	public ChatMessagesList getChatMessagesList() {
		return chatMessagesList;
	}

	public UserRanking getUserRanking() {
		return userRanking;
	}

	public void reactTo(Event e) throws EventNotHandledException {
		reactor.handle(e);
	}
	
	public void sendFakeChatMessage(MessageSendEvent ev) {
		chatMessagesList.reactTo(ev);
	}

	public void setController(AppController c) {
		this.controller = c;
	}

	public void registerChatBox(AppView v) {
		chatMessagesList.setView(v.getMainWindow().getChatBox());
	}

	public void registerRanking(AppView v) {
		userRanking.setView(v.getMainWindow().getRanking());
	}
	
	public void registerDrawingPanel(AppView v) {
		drawingModel.setDrawingPanel(v.getMainWindow().getDrawingPanel());
	}

}
