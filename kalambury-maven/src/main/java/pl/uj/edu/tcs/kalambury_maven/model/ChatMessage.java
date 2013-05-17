package pl.uj.edu.tcs.kalambury_maven.model;


/**
 * Class which represents one chat message.
 * 
 * @author Anna Szybalska
 *
 */
public class ChatMessage {

	public static enum TYPE {
		SM_WROTE, SM_ONLINE, SM_OFFLINE, SM_DRAW, SM_GUESSED
	}

	private String user;
	private TYPE type;
	private String message;
	
	public ChatMessage(String user, TYPE type, String ... message) {
		this.user = user;
		this.type = type;
		if (type == TYPE.SM_WROTE) {
			this.message = message[0];
		} else {
			message = null;
		}
	}

	public String getUser() {
		return user;
	}

	public TYPE getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

}