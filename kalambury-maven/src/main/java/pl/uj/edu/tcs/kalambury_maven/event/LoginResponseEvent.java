package pl.uj.edu.tcs.kalambury_maven.event;

public class LoginResponseEvent implements Event {

	private static final long serialVersionUID = 7639878621412627702L;
	private final LoginAttemptEvent attempt;
	private final boolean response;
	
	public LoginResponseEvent(LoginAttemptEvent attempt, boolean response) {
		this.attempt = attempt;
		this.response = response;
	}
	public LoginAttemptEvent getAttempt() {
		return attempt;
	}
	public boolean getResponse() {
		return response;
	}
}
