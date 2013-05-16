package pl.uj.edu.tcs.kalambury_maven.event;

public class NewMessageWrittenEvent implements Event{
	private static final long serialVersionUID = 912391702381671L;
	
	private String user;
	private String message;
	
	
	public NewMessageWrittenEvent(String user, String message) {
		this.user = user;
		this.message = message;
	}


	public String getUser() {
		return user;
	}


	public String getMessage() {
		return message;
	}
	
}
