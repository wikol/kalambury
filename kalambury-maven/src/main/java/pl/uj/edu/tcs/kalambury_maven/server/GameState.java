package pl.uj.edu.tcs.kalambury_maven.server;

public class GameState {
	
	private int round;
	private long currentRoundTimeLeft;
	private int numberOfPlayers;
	private String drawingUser;
	
	/**
	 * Aktualnie przyjmuje tylko aktualnie rysującego usera, ale można to będzie rozszerzyć.
	 * 
	 * @param drawingUser
	 */
	public GameState(
			String drawingUser) {
		super();
		this.drawingUser = drawingUser;
	}

	public int getRound() {
		return round;
	}

	public long getCurrentRoundTimeLeft() {
		return currentRoundTimeLeft;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public String getDrawingUser() {
		return drawingUser;
	}
}
