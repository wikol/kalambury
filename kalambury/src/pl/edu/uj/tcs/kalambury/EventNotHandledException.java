package pl.edu.uj.tcs.kalambury;

public class EventNotHandledException extends Exception {

	private static final long serialVersionUID = 7307495094763597012L;
	public EventNotHandledException() {}
	public EventNotHandledException(String msg) {
		super(msg);
	}
}
