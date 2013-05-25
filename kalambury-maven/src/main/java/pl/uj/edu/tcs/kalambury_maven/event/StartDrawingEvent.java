package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Rozsyłane przez serwer, kiedy ten wie już kto i jak ma rysować, oraz dostał
 * hasło które będzie rysowane. W tym momencie użytkownik powinnien dostać
 * możliwość rysowania, inni użytkownicy mogą dostać monit o tym że faktyczne
 * rysowanie sie zaczęło.
 * 
 * @author Anna Szybalska
 * 
 */
public class StartDrawingEvent implements Event {
	private static final long serialVersionUID = 554207634409285942L;
	
	private String user;
	
	/**
	 * Konstruktor, przyjmuje usera który właśnie zaczyna rysować.
	 * @param user
	 */
	public StartDrawingEvent(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}

}
