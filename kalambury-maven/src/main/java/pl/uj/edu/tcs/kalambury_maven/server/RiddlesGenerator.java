package pl.uj.edu.tcs.kalambury_maven.server;

public class RiddlesGenerator {
	private String currentRiddle;
	
	public String getCurrentRiddle() {
		return currentRiddle;
	}
	
	public String nextRiddle() {
		currentRiddle = "następna zagadka";
		return currentRiddle;
	}

}
