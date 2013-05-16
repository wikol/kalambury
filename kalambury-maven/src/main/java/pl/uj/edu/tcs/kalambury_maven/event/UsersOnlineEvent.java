package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Event do informowania o nowych usersach którzy właśnie stali się online.
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

	public List<String> getUsersOnline() {
		return usersOnline;
	}
}
