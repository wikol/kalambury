package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import pl.uj.edu.tcs.kalambury_maven.event.ClearScreenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.CloseWordInputEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventReactor;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordForGuessingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordIsNeededEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;
import pl.uj.edu.tcs.kalambury_maven.event.PointsChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.RiddleEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.helpers.Log;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;

public class GameLogic {
	private static final String CHAT_SERVER_NAME = "SERVER INFO";

	private Server server;
	private Queue<String> drawingQueue = new LinkedBlockingDeque<>();
	private SimpleModel localModel = new SimpleModel();
	private RoundTimer roundTimer = new RoundTimer(this);

	private RiddlesGenerator riddlesGenerator = new RiddlesGenerator();
	private PointsManager pointsManager = new PointsManager(localModel.getUserRanking());

	private boolean gameStarted = false;

	public synchronized void reactTo(String username, Event event) {

		Log.i("Otrzymano " + event.toString());
		try {

		if (event instanceof NewGameEvent) {
			newGame(username, (NewGameEvent) event);
		}

		if (event instanceof UsersOnlineEvent) {
			usersOnline(username, (UsersOnlineEvent) event);
		}

		if (event instanceof UsersOfflineEvent) {
			usersOffline(username, (UsersOfflineEvent) event);
		}

		if (event instanceof NewMessageWrittenEvent) {
			newMessage(username, (NewMessageWrittenEvent) event);
		}

		if (event instanceof NewPointsDrawnEvent) {
			newPoints(username, (NewPointsDrawnEvent) event);
		}

		if (event instanceof ClearScreenEvent) {
			clearScreen(username, (ClearScreenEvent) event);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void newGame(String username, NewGameEvent event) {
		startNextRound();
		gameStarted = true;
		return;
	}

	private void usersOnline(String username, UsersOnlineEvent event) {
		Log.i("Nowy user online: " + username);
		// dodanie do kolejki rysujących
		drawingQueue.add(username);

		localModel.getUserRanking().addNewUser(username);
		Log.i("wysylamy do " + username + " taki ranking: "
				+ localModel.getUserRanking().getUsersOnline());
		server.sendEvent(username, new ResetUserRankingEvent(localModel
				.getUserRanking().getFullRanking()));

		// wysyłanie użytkownikowi całego rysunku
		Event wholeDrawingEvent = new NewPointsDrawnEvent(localModel
				.getDrawingModel().getDrawing());
		server.sendEvent(username, wholeDrawingEvent);

		// wysyłanie mu informacji o aktualnej rundzie
		server.sendEvent(
				username,
				new NextRoundStartsEvent(drawingQueue.peek(), roundTimer
						.getTimeLeft(), roundTimer.getTimeLeft()));

		server.broadcastEvent(event);

		return;
	}

	private void usersOffline(String username, UsersOfflineEvent event) {
		Log.i(username + " stal sie offline");
		// jeśli zniknął użytkownik właśnie rysujacy
		if (username.equals(drawingQueue.peek())) {
			drawingQueue.poll();
			roundTimer.stopTimer();
			server.broadcastEvent(new MessageSendEvent(CHAT_SERVER_NAME,
					"Current drawing user left game, skiping to next round."));

			if (!drawingQueue.isEmpty()) {
				startNextRound();
			} else {
				Log.i("Zero uzytkownikow na serwerze");
			}

		} else {
			// usunięcie z kolejki rysujących
			drawingQueue.remove(username);
		}

		// wywalenie z rankingu
		localModel.getUserRanking().deleteUser(username);

		server.broadcastEvent(event);
		return;
	}

	private void newMessage(String username, NewMessageWrittenEvent event) {
		/*
		 * nie przyjmujemy wiadomości od użytkownika, który rysuje - jeśli on
		 * chciał coś napisać, informacja, że mu nie wolno.
		 */
		if (!drawingQueue.isEmpty() && username.equals(drawingQueue.peek())) {
			server.sendEvent(username, new MessageSendEvent(CHAT_SERVER_NAME,
					"You CAN'T write on chat while drawin'!"));
			Log.i(username + " probowal napisac, choc aktualnie rysuje");
			return;
		}
		MessageSendEvent messageEvent = new MessageSendEvent(username,
				event.getMessage());
		localModel.getChatMessagesList().reactTo(messageEvent);

		server.broadcastEvent(messageEvent);

		/*
		 * jeśli użytkownik zgadł (nie patrzymy na wielkość liter i białe znaki
		 * na końcu i początku hasła) i trwa aktualnie sesja rysowania
		 */
		String newMessage = event.getMessage().trim().toLowerCase();
		String currentWord = riddlesGenerator.getCurrentRiddle().trim()
				.toLowerCase();
		if (newMessage.equals(currentWord)) {
			roundTimer.stopTimer();
			String drawingUser = switchUserFromTop();
			pointsManager.updateRankingAfterGuessing(username, new GameState(
					drawingUser));
			server.broadcastEvent(new WordGuessedEvent(riddlesGenerator
					.getCurrentRiddle(), username, drawingUser));
			startNextRound();
			return;
		}
		// później - sprawdź, czy to nie koniec gry

		// sprawdzanie czy słowa są podobne
		if (areSimilar(newMessage, currentWord)) {
			server.sendEvent(username, new MessageSendEvent(CHAT_SERVER_NAME,
					"You nearly guessed!"));
		}

		return;
	}

	private void newPoints(String username, NewPointsDrawnEvent event) {
		// nie jesteśmy w trakcie sesji rysowania
		if (!gameStarted || drawingQueue.isEmpty()) {
			return;
		}

		// bo osoba była zła, a zupa za słona
		if (!username.equals(drawingQueue.peek())) {
			return;
		}

		localModel.getDrawingModel().actualiseDrawing(
				((NewPointsDrawnEvent) event).getPoints());
		server.broadcastEvent(event);
		return;
	}

	private void clearScreen(String username, ClearScreenEvent event) {
		if (!username.equals(drawingQueue.peek())) {
			return;
		}

		server.broadcastEvent(event);
	}

	/**
	 * Funkcja do wołania przez timer odliczający czas rundy, informująca o tym
	 * że runda właśnie się skończyła.
	 */
	public synchronized void roundTimeIsOver() {
		// znaczy, że ktoś jednak zgadł, tylko źle się nam zsynchronizowało
		if (roundTimer.getTimeLeft() > 1000)
			return;

		// coś się rozsynchronizowało
		if (!gameStarted)
			return;

		String drawingUser = switchUserFromTop();

		pointsManager.updateRankingAfterTimeOver(new GameState(drawingUser));
		server.broadcastEvent(new PointsChangedEvent(drawingUser, localModel
				.getUserRanking().getPointsForUser(drawingUser)));

		server.sendEvent(drawingUser, new MessageSendEvent(CHAT_SERVER_NAME,
				"Your picture was bad, your points decreased"));
		server.broadcastEvent(new MessageSendEvent(CHAT_SERVER_NAME,
				"No one guessed, skiping to next round."));
		startNextRound();
	}

	public void setServer(Server s) {
		server = s;
	}

	/**
	 * Funkcja pomocnicza, do startowania następnej rundy, z graczem aktualnie u
	 * góry kolejki jako rysującym. Czyści planszę, prosi o hasło do rysowania.
	 */
	private void startNextRound() {
		localModel.getDrawingModel().clearScreen();
		server.broadcastEvent(new ClearScreenEvent());

		roundTimer.startRound();

		localModel.getUserRanking().nextRound(drawingQueue.peek());
		server.broadcastEvent(new NextRoundStartsEvent(drawingQueue.peek(),
				roundTimer.getRoundTime(), roundTimer.getRoundTime()));

		server.sendEvent(drawingQueue.peek(), new RiddleEvent(riddlesGenerator.nextRiddle()));

	}

	private String switchUserFromTop() {
		String user = drawingQueue.poll();
		drawingQueue.add(user);
		return user;
	}

	// do testów
	public SimpleModel getModel() {
		return this.localModel;
	}

	public Queue<String> getQueue() {
		return this.drawingQueue;
	}

	private boolean areSimilar(String guess, String toGuess) {
		guess = guess.trim().toLowerCase();
		toGuess = toGuess.trim().toLowerCase();
		char[] before = { 'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'ż', 'ź' };
		char[] after = { 'a', 'c', 'e', 'l', 'n', 'o', 's', 'z', 'z' };
		for (int i = 0; i < before.length; i++) {
			guess = guess.replace(before[i], after[i]);
			toGuess = toGuess.replace(before[i], after[i]);
		}
		int[][] common = new int[guess.length() + 1][toGuess.length() + 1];
		for (int i = 1; i <= guess.length(); i++)
			for (int j = 1; j <= toGuess.length(); j++) {
				if (guess.charAt(i - 1) == toGuess.charAt(j - 1)) {
					common[i][j] = common[i - 1][j - 1] + 1;
				} else {
					common[i][j] = Math.max(common[i - 1][j], common[i][j - 1]);
				}
			}
		int result = common[guess.length()][toGuess.length()];
		return guess.substring(0, Math.min(3, guess.length())).equals(
				toGuess.substring(0, Math.min(toGuess.length(), 3)))
				&& result >= (toGuess.length() + 1) / 2;
	}

}
