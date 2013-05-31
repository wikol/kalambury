package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * 
 * @author Anna Szybalska
 * 
 */
public class CloseWordInputEvent implements Event {
	private static final long serialVersionUID = 554207634409285942L;
	
	private String user;
	
	/**
	 * Konstruktor, przyjmuje usera który właśnie zaczyna rysować.
	 * @param user
	 */
	public CloseWordInputEvent(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}

}
