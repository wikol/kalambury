package pl.uj.edu.tcs.kalambury_maven.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa odpowiadająca za przechowywanie informacji o użytkownikach online
 * - ich nazwach i liczbie uzyskanych punktów
 * 
 * @author Anna Dymek
 */

public class UserRanking {
	private static Map<String, Integer> users = new HashMap<String, Integer>();

	/**
	 * Dodawanie nowego użytkownika
	 * 
	 * @param name
	 * 				- nazwa nowego użytkownika, która nie może składać się z samych białych znaków
	 * 				  ani być dłuższa niż 20 znaków
	 * @return true
	 * 				- udało się dodać użytkownika
	 * @return false
	 * 
	 */
	public boolean addNewUser(String name) {
		if((name.trim().length() > 0 && name.length() <= 20) && !users.containsKey(name)) {
			users.put(name, 0);
			return true;
		}
		return false;
		
	}
	
	/**
	 * Zwiększanie liczby punktów zawodnikowi
	 * @param name
	 * 				- imię zawodnika
	 * @param points
	 * 				- ile ekstra punktów dostał zawodnik
	 */
	public void addPointsToUser(String name, int points) {
		points += users.get(name);
		users.put(name, points);
	}

	/**
	 * Usuwanie zawodnika
	 * @param name
	 * 				- imię usuwanego zawodnika
	 */
	public void deleteUser(String name) {
		users.remove(name);
	}
}
