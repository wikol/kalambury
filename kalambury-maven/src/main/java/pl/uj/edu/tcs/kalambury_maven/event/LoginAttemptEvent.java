package pl.uj.edu.tcs.kalambury_maven.event;

public class LoginAttemptEvent implements Event {

	private static final long serialVersionUID = -7207316247303912954L;
	private final String server, port, login;

	public LoginAttemptEvent(String server, String port, String login) {
		this.server = server;
		this.port = port;
		this.login = login;
	}

	public String getServer() {
		return server;
	}

	public String getPort() {
		return port;
	}

	public String getLogin() {
		return login;
	}
}
