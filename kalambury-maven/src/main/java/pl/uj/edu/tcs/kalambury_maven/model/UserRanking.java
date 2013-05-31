package pl.uj.edu.tcs.kalambury_maven.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Integer prevPoints = users.get(name);
		if (prevPoints != null) {
			points += prevPoints;
		}
		users.put(name, points);
		updateView();
	}

	/**
	 * Ustawianie liczby punktów zawodnikowi
	 * 
	 * @param name
	 *            - imię zawodnika
	 * @param points
	 *            - ile w sumie punktów ma zawodnik
	 */
	public void setPointsForUser(String name, int points) {
		users.put(name, points);
		updateView();
	}

	/**
	 * Zwraca ilość punktów zawodnika
	 * 
	 * @param name
	 *            - imię zawodnika
	 * @return ilość punktów zawodnika name
	 */
	public int getPointsForUser(String name) {
		return users.get(name);
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
	 * @return List użytkowników zalogowanych
	 */
	public List<String> getUsersOnline() {
		return new ArrayList<>(users.keySet());
	}

	/**
	 * Zwraca mapę użytkowników-punktów, przydatne gdy w trakcie gry loguje się
	 * nowy user.
	 * 
	 * @return
	 */
	public Map<String, Integer> getFullRanking() {
		return users;
	}

	/**
	 * Resetowanie rankingu so stanu podanego w mapie usersPoints. Potrzebne gdy
	 * do toczącej się gry dochodzi nagle nowy user.
	 * 
	 * @param usersPoints
	 *            - mapa zawierajaca userów i ich punkty
	 */
	public void setUsersPoints(Map<String, Integer> usersPoints) {
		users = new HashMap<>(usersPoints);
		updateView();
	}
}
