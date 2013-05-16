package pl.uj.edu.tcs.kalambury_maven.event;

public class EventNotHandledException extends RuntimeException {

	private static final long serialVersionUID = 7307495094763597012L;
	public EventNotHandledException() {}
	public EventNotHandledException(String msg) {
		super(msg);
	}
}
