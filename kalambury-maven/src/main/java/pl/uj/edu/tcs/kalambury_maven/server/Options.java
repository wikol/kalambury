package pl.uj.edu.tcs.kalambury_maven.server;

public class Options {
	private static Options instance = null;
	private Options () {
	}
	
	public static Options getInstance() {
		if (instance == null) {
			instance = new Options();
		}
		return instance;
	}
	
	private long roundTimeInMilisec = 180000;
	private int pointsForGuessing = 5;
	private int pointsForDrawing = 10;
	private int pointsForUnguessedDrawing = -5;
	
	public long getRoundTimeInMilisec() {
		return roundTimeInMilisec;
	}

	public void setRoundTimeInMilisec(long roundTimeInMilisec) {
		this.roundTimeInMilisec = roundTimeInMilisec;
	}

	public int getPointsForGuessing() {
		return pointsForGuessing;
	}

	public void setPointsForGuessing(int pointsForGuessing) {
		this.pointsForGuessing = pointsForGuessing;
	}

	public int getPointsForDrawing() {
		return pointsForDrawing;
	}

	public void setPointsForDrawing(int pointsForDrawing) {
		this.pointsForDrawing = pointsForDrawing;
	}

	public int getPointsForUnguessedDrawing() {
		return pointsForUnguessedDrawing;
	}

	public void setPointsForUnguessedDrawing(int pointsForUnguessedDrawing) {
		this.pointsForUnguessedDrawing = pointsForUnguessedDrawing;
	}
}
