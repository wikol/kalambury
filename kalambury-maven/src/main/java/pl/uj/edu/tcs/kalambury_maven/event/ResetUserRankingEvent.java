package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Event rozsyłany przez serwer, służący do resetowania rankingu u klienta do stanu rankingu na serwerze.
 * @author Anna Szybalska
 *
 */
public class ResetUserRankingEvent implements Event {
	private static final long serialVersionUID = 4173436537282424670L;
	
	private Map<String, Integer> usersPoints;
	
	public ResetUserRankingEvent(Map<String, Integer> usersPoints) {
		this.usersPoints = new HashMap<>(usersPoints);
	}
	
	public Map<String, Integer> getUsersPoints() {
		return usersPoints;
	}

}
