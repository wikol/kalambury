package pl.uj.edu.tcs.kalambury_maven.server;

import pl.uj.edu.tcs.kalambury_maven.model.UserRanking;

public interface PointsManager {

	void updateRankingAfterGuessing(String guesser, GameState gs);

	void updateRankingAfterTimeOver(GameState gs);

	void setRanking(UserRanking rank);
}
