package pl.uj.edu.tcs.kalambury_maven.event;

public class LoginUnsuccessfulEvent implements Event {

	private static final long serialVersionUID = -1654534748153623103L;
	private final String msg;
	public LoginUnsuccessfulEvent(String msg) {
		this.msg = msg;
	}
	public String getMessage() {
		return msg;
	}
}
