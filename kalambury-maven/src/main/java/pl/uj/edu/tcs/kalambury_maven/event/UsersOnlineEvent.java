package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Event do informowania o nowych usersach którzy właśnie stali się online.
 * 
 * Można używać jako pojemnik na wielu userów albo na jednego usera.
 * Wielu userów szczególnie przydatne w testach.
 * Przykładowe użycie:
 * UserOnlineEvent event = new UserOnlineEvent("user");
 * String usr = event.getUser();
 * 
 * Lub:
 * UserOnlineEvent event = new UserOnlineEvent("user1", "user2", "user3");
 * List<String> users = event.getUsersOnline();
 * 
 * @author Anna Szybalska
 *
 */
public class UsersOnlineEvent implements Event {

	private static final long serialVersionUID = -6250441893681105618L;
	
	public List<String> usersOnline;
	
	public UsersOnlineEvent(List<String> users) {
		usersOnline = new ArrayList<>(users);
	}
	
	public UsersOnlineEvent(String ... users) {
		usersOnline = new ArrayList<>(Arrays.asList(users));
	}

	/**
	 * Zwraca wszystkich userów z tego eventu.
	 * @return lista wszystkich userów jako
	 */
	public List<String> getUsersOnline() {
		return usersOnline;
	}
	
	/**
	 * Zwraca pierwszego usera z listy userów
	 * @return pierwszy user z listy
	 */
	public String getUser() {
		return usersOnline.get(0);
	}
}
