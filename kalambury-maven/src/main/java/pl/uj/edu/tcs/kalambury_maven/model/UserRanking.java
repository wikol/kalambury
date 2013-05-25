package pl.uj.edu.tcs.kalambury_maven.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import pl.uj.edu.tcs.kalambury_maven.view.Ranking;

/**
 * Klasa odpowiadająca za przechowywanie informacji o użytkownikach online - ich
 * nazwach i liczbie uzyskanych punktów
 * 
 * @author Anna Dymek
 */

public class UserRanking {
	private Ranking myView;
	private String nowDrawing = "?";
	private static Map<String, Integer> users = new HashMap<String, Integer>();

	private void updateView() {
		if (myView == null)
			return;
		myView.displayRanking(users, nowDrawing);
	}

	public void setView(Ranking myView) {
		this.myView = myView;
	}

	/**
	 * Ustawienie nowego rysującego
	 * 
	 * @param name
	 *            - imię rysującego
	 */
	public void nextRound(String name) {
		nowDrawing = name;
		updateView();
	}

	/**
	 * Dodawanie nowego użytkownika
	 * 
	 * @param name
	 *            - nazwa nowego użytkownika, która nie może składać się z
	 *            samych białych znaków ani być dłuższa niż 20 znaków
	 * @return true - udało się dodać użytkownika
	 * @return false
	 * 
	 */
	public boolean addNewUser(String name) {
		if ((name.trim().length() > 0 && name.length() <= 20)
				&& !users.containsKey(name)) {
			users.put(name, 0);
			updateView();
			return true;
		}
		return false;
	}

	/**
	 * Zwiększanie liczby punktów zawodnikowi
	 * 
	 * @param name
	 *            - imię zawodnika
	 * @param points
	 *            - ile ekstra punktów dostał zawodnik
	 */
	public void addPointsToUser(String name, int points) {
		points += users.get(name);
		users.put(name, points);
		updateView();
	}

	/**
	 * Usuwanie zawodnika
	 * 
	 * @param name
	 *            - imię usuwanego zawodnika
	 */
	public void deleteUser(String name) {
		users.remove(name);
		updateView();
	}

	/**
	 * Sprawdzanie, czy dany użytkownik jest zalogowany
	 * 
	 * @param name
	 * @return
	 */
	public boolean isUserOnline(String name) {
		return users.containsKey(name);
	}

	/**
	 * Wylistowanie użytkowników
	 * 
	 * @return ArrayList użytkowników zalogowanych
	 */
	public ArrayList<String> getUsersOnline() {
		return new ArrayList<>(users.keySet());
	}
}
