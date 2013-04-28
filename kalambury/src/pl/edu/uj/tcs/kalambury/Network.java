package pl.edu.uj.tcs.kalambury;

/**
 * Interfejs odpowiedzialny za wysyłanie danych na serwer
 * @author Wiktor Kuropatwa
 *
 */
public interface Network {
	public void sendToServer(Event e);
}
