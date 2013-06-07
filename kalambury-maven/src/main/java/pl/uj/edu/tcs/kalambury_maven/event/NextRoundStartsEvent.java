package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Event rozsyłany przez serwer, do informowania klientów o tym że nowa runda
 * się zaczęła.
 * 
 * @author Anna Szybalska
 * 
 */
public class NextRoundStartsEvent implements Event {

	private static final long serialVersionUID = 3952628287399438137L;
	
	private String drawingUser;
	private long timeLeft;
	private long roundTime;
	private String riddle;

	/**
	 * Konstruktor, przyjmuje nick użytkownika który rysuje w rundzie właśnie
	 * się rozpoczynającej.
	 * 
	 * @param drawingUser
	 */
	public NextRoundStartsEvent(String drawingUser, long timeLeft, long roundTime) {
		this.drawingUser = drawingUser;
		this.timeLeft = timeLeft;
		this.roundTime = roundTime;
	}

	public String getDrawingUser() {
		return drawingUser;
	}
	
	public long getTimeLeft() {
		return timeLeft;
	}
	
	public long getRoundTime() {
		return roundTime;
	}

}
