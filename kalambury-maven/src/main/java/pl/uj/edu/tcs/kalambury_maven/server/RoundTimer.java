package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Timer;

public class RoundTimer {

	private long roundTime = 180;
	private GameLogic gameLogic;
	private Timer timer;

	public RoundTimer(GameLogic gameLogic) {
		timer = new Timer();
		this.gameLogic = gameLogic;
	}
	
	public void startRound() {
	}
	
	public void setRoundTime(long roundTime) {
		this.roundTime = roundTime;
	}
	
	public long getRoundTime() {
		return roundTime;
	}
	
}