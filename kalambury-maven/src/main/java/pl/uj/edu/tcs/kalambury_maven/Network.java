package pl.uj.edu.tcs.kalambury_maven;

/**
 * Interfejs odpowiedzialny za wysyłanie danych na serwer
 * @author Wiktor Kuropatwa
 *
 */
public interface Network {
	public void sendToServer(Event e);
}
