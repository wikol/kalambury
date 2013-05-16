package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Event which should be send from network that new message arrived to server.
 * 
 * @author Anna Szybalska
 */

public class MessageSendEvent implements Event {
	private static final long serialVersionUID = 836955177507854000L;
	
	private String user;
	private String message;
	
	public MessageSendEvent(String user, String message) {
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
