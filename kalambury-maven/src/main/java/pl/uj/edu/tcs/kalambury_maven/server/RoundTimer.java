package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoundTimer {

	private long roundTime = 180;
	private GameLogic gameLogic;
	private ScheduledExecutorService timer;
	private ScheduledFuture<Object> future = null;
	private long lastRoundStart;

	public RoundTimer(GameLogic gameLogic) {
		timer = Executors.newScheduledThreadPool(5);
		this.gameLogic = gameLogic;
	}

	public void startRound() {
		if (future != null && !future.isCancelled()) {
			future.cancel(true);
		}

		future = timer.schedule(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				gameLogic.roundTimeIsOver();
				return null;
			}
		}, roundTime, TimeUnit.MILLISECONDS);
		lastRoundStart = System.currentTimeMillis();
	}

	public long getTimeLeft() {
		if (lastRoundStart > 0) {
			return roundTime - (System.currentTimeMillis() - lastRoundStart);
		}
		return 0;
	}

	public void setRoundTime(long roundTime) {
		this.roundTime = roundTime;
	}

	public long getRoundTime() {
		return roundTime;
	}

	private class RoundIsOver implements Callable<Object> {
		private GameLogic gameLogic;

		public RoundIsOver(GameLogic gL) {
			this.gameLogic = gL;
		}

		@Override
		public RoundIsOver call() throws Exception {
			gameLogic.roundTimeIsOver();
			return null;
		}

	}

}