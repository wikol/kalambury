package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Event do informowania o usersach którzy właśnie stali się offline.
 * 
 * Można używać jako pojemnik na wielu userów albo na jednego usera.
 * Wielu userów szczególnie przydatne w testach.
 * Przykładowe użycie:
 * UserOfflineEvent event = new UserOfflineEvent("user");
 * String usr = event.getUser();
 * Lub:
 * UserOfflineEvent event = new UserOfflineEvent("user1", "user2", "user3");
 * List<String> users = event.getUsersOffline();
 * 
 * @author Anna Szybalska
 *
 */
public class UsersOfflineEvent implements Event {
	private static final long serialVersionUID = 3882081983481015206L;
	
	private List<String> usersOffline;
	
	public UsersOfflineEvent(List<String> users) {
		usersOffline = new ArrayList<>(users);
	}
	
	public UsersOfflineEvent(String ... users) {
		usersOffline = new ArrayList<>(Arrays.asList(users));
	}

	/**
	 * Zwraca wszystkich userów z tego eventu.
	 * @return lista wszystkich userów jako
	 */
	public List<String> getUsersOffline() {
		return usersOffline;
	}
	
	/**
	 * Zwraca pierwszego usera z listy userów
	 * @return pierwszy user z listy
	 */
	public String getUser() {
		return usersOffline.get(0);
	}
	
}
