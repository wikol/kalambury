package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventHandler;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;
import pl.uj.edu.tcs.kalambury_maven.view.View;

public class AppController implements Controller {
	private EventReactor reactor = new EventReactor();
	private View view;
	private Model model;
	
	public AppController() {
		reactor.setHandler(NewMessageWrittenEvent.class, new EventHandler() {
			
			@Override
			public void handle(Event e) {
				NewMessageWrittenEvent ev = (NewMessageWrittenEvent) e;
				((SimpleModel)model).sendFakeChatMessage(new MessageSendEvent(ev.getUser(), ev.getMessage()));
			}
		});
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

}
