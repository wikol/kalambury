package pl.uj.edu.tcs.kalambury_maven.server;

import pl.uj.edu.tcs.kalambury_maven.event.Event;

public class DummyGameLogic {
	public synchronized void reactTo(String name, Event event) {
		System.out.println("reacting to " + event + " from " + name);
	}
}
