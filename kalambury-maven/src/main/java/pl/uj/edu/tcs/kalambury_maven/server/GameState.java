package pl.uj.edu.tcs.kalambury_maven.server;

public class GameState {
	private int round;
	private long currentRoundTime;
	private int numberOfPlayers;
	private String drawingUser;
	
	public GameState(int round, long currentRoundTime, int numberOfPlayers,
			String drawingUser) {
		super();
		this.round = round;
		this.currentRoundTime = currentRoundTime;
		this.numberOfPlayers = numberOfPlayers;
		this.drawingUser = drawingUser;
	}

	public int getRound() {
		return round;
	}

	public long getCurrentRoundTime() {
		return currentRoundTime;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public String getDrawingUser() {
		return drawingUser;
	}
}
