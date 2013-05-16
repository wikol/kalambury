package pl.uj.edu.tcs.kalambury_maven.network;

public class UnableToConnectException extends Exception {

	private static final long serialVersionUID = -7663578911719785911L;
	public UnableToConnectException() {}
	public UnableToConnectException(String msg) {
		super(msg);
	}
}
