package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Event to inform that someone guessed correctly.
 * 
 * @author Anna Szybalska
 */

public class WordGuessedEvent implements Event {
	private static final long serialVersionUID = 7421449865248854974L;
	
	private String word;
	private String user;
	private String drawingUser;
	
	/**
	 * Constructor of WordGuessedEvent, it takes guessed word and nick of user as a argument
	 * @param word guessed word.
	 * @param user nick of user who guessed
	 */
	public WordGuessedEvent(String word, String user, String drawingUser) {
		this.word = word;
		this.user = user;
		this.drawingUser = drawingUser;
	}

	public String getWord() {
		return word;
	}

	public String getUser() {
		return user;
	}


	public String getDrawingUser() {
		return drawingUser;
	}
}
