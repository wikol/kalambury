package pl.uj.edu.tcs.kalambury_maven.event;

public class PointsChangedEvent implements Event {
	private static final long serialVersionUID = -8581938987385752977L;
	
	private String user;
	private int newPointsNumber;
	
	public PointsChangedEvent(String user, int newPointsNumber) {
		this.user = user;
		this.newPointsNumber = newPointsNumber;
	}

	public String getUser() {
		return user;
	}
	
	public int getNewPointsNumber() {
		return newPointsNumber;
	}
}
