package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoundTimer {

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
			stopTimer();
		}

		future = timer
				.schedule(new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						System.out.println("Czas się skończył");
						gameLogic.roundTimeIsOver();
						return null;
					}
				}, Options.getInstance().getRoundTimeInMilisec(),
						TimeUnit.MILLISECONDS);
		lastRoundStart = System.currentTimeMillis();
	}

	public void stopTimer() {
		if (future != null) {
			future.cancel(true);
		}
	}

	public long getTimeLeft() {
		if (lastRoundStart > 0) {
			return Options.getInstance().getRoundTimeInMilisec()
					- (System.currentTimeMillis() - lastRoundStart);
		}
		return 0;
	}

	public long getRoundTime() {
		return Options.getInstance().getRoundTimeInMilisec();
	}
}