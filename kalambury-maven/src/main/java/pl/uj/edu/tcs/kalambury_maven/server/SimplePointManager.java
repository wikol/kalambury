package pl.uj.edu.tcs.kalambury_maven.server;

import pl.uj.edu.tcs.kalambury_maven.model.UserRanking;

public class SimplePointManager implements PointsManager {
	private UserRanking userRanking;

	@Override
	public void updateRankingAfterGuessing(String guesser, GameState gs) {
		Options options = Options.getInstance();
		System.out.println("dis' getz called");
		userRanking.addPointsToUser(guesser, options.getPointsForGuessing());
		userRanking.addPointsToUser(gs.getDrawingUser(), options.getPointsForDrawing());
	}
	
	@Override
	public void updateRankingAfterTimeOver(GameState gs) {
		Options options = Options.getInstance();
		userRanking.addPointsToUser(gs.getDrawingUser(), options.getPointsForUnguessedDrawing());
	}

	@Override
	public void setRanking(UserRanking rank) {
		userRanking = rank;
	}
	
}
