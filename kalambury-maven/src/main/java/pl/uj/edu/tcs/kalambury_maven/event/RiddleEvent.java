package pl.uj.edu.tcs.kalambury_maven.event;

public class RiddleEvent implements Event {
	private static final long serialVersionUID = -8874462649571111001L;
	private String riddle;
	
	public RiddleEvent(String riddle) {
		this.riddle = riddle;
	}
	
	public String getRiddle() {
		return riddle;
	}

}
