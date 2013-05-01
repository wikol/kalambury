package pl.uj.edu.tcs.kalambury_maven;

public class EventNotHandledException extends Exception {

	private static final long serialVersionUID = 7307495094763597012L;
	public EventNotHandledException() {}
	public EventNotHandledException(String msg) {
		super(msg);
	}
}
