package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Interfejs definiujący zachowanie w przypadku zaistnienia danego eventu.
 * @author Wiktor Kuropatwa
 *
 */
public interface EventHandler {
	/**
	 * Jedyna metoda do zaimplemetowania - kod, który ma być wykonywany w przypadku
	 * wystąpienia danego eventu
	 * @param e event, który nastąpił
	 */
	public void handle(Event e);
}
