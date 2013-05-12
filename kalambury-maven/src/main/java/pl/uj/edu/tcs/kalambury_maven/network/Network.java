package pl.uj.edu.tcs.kalambury_maven.network;

import pl.uj.edu.tcs.kalambury_maven.event.Event;

/**
 * Interfejs odpowiedzialny za wysyłanie danych na serwer
 * @author Wiktor Kuropatwa
 *
 */
public interface Network {
	public void sendToServer(Event e);
}
