package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Event do informowania o usersach którzy właśnie stali się offline.
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

	public List<String> getUsersOffline() {
		return usersOffline;
	}
	
}
