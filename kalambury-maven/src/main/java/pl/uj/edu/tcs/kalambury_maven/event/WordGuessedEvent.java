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
	private int pointsForWord;
	private String drawingUser;
	private int pointsForDrawing;
	
	/**
	 * Constructor of WordGuessedEvent, it takes guessed word and nick of user as a argument
	 * @param word guessed word.
	 * @param user nick of user who guessed
	 */
	public WordGuessedEvent(String word, String user, int pointsForWord, String drawingUser, int pointsForDrawing) {
		this.word = word;
		this.user = user;
		this.pointsForWord = pointsForWord;
		this.drawingUser = drawingUser;
		this.pointsForDrawing = pointsForDrawing;
	}

	public String getWord() {
		return word;
	}

	public String getUser() {
		return user;
	}

	public int getPointsForWord() {
		return pointsForWord;
	}

	public String getDrawingUser() {
		return drawingUser;
	}

	public int getPointsForDrawing() {
		return pointsForDrawing;
	}
}
