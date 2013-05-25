package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Event który serwer wysyła użytkownikowi który będzie aktualnie rysował.
 * Użytkownik powinien wtedy podać słowo które chce rysować.
 * 
 * @author Anna Szybalska
 * 
 */
public class NewWordIsNeededEvent implements Event {
	private static final long serialVersionUID = -7785554478992365010L;
}
