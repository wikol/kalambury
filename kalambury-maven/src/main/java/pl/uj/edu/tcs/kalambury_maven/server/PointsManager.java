package pl.uj.edu.tcs.kalambury_maven.server;

import pl.uj.edu.tcs.kalambury_maven.model.UserRanking;

public class PointsManager {
	private UserRanking userRanking;
	
	public PointsManager(UserRanking ranking) {
		this.userRanking = ranking;
	}

	public void updateRankingAfterGuessing(String guesser, GameState gs) {
		Options options = Options.getInstance();
		userRanking.addPointsToUser(guesser, options.getPointsForGuessing());
		userRanking.addPointsToUser(gs.getDrawingUser(), options.getPointsForDrawing());
	}
	
	public void updateRankingAfterTimeOver(GameState gs) {
		Options options = Options.getInstance();
		userRanking.addPointsToUser(gs.getDrawingUser(), options.getPointsForUnguessedDrawing());
	}
	
}
