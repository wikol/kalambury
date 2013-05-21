package pl.uj.edu.tcs.kalambury_maven.server;

import pl.uj.edu.tcs.kalambury_maven.event.Event;

public interface Server {
	public void sendEvent(String name, Event event);
	public void broadcastEvent(Event event);
}
