package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import pl.uj.edu.tcs.kalambury_maven.event.ClearScreenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordForGuessingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordIsNeededEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NextRoundStartsEvent;
import pl.uj.edu.tcs.kalambury_maven.event.PointsChangedEvent;
import pl.uj.edu.tcs.kalambury_maven.event.ResetUserRankingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.CloseWordInputEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;

public class GameLogic {
	private static final String CHAT_SERVER_NAME = "!!!_SERVER";

	private Server server;
	private Queue<String> drawingQueue = new LinkedBlockingDeque<>();
	private SimpleModel localModel = new SimpleModel();
	private RoundTimer roundTimer = new RoundTimer(this);

	private String nowBeingDrawnWord = ""; // aktualne hasło

	// true jeśli aktualnie ktoś rysuje i można zgadywać hasło
	private boolean someoneIsDrawing = false;
	private boolean gameStared = false;

	public synchronized void reactTo(String username, Event event) {

		loguj("Otrzymano " + event.toString());

		if (event instanceof NewGameEvent) {
			startNextRound();
			gameStared = true;
			return;
		}

		if (event instanceof NewWordForGuessingEvent) {
			loguj("nowe hasełko otrzymano od " + username);
			// jeśli hasło wysłał nam nie ten kto teraz ma rysować to ignorujemy
			if (!username.equals(drawingQueue.peek()))
				return;

			nowBeingDrawnWord = ((NewWordForGuessingEvent) event).getWord();
			someoneIsDrawing = true;
			server.sendEvent(drawingQueue.peek(), new CloseWordInputEvent(
					drawingQueue.peek()));
			return;
		}

		if (event instanceof UsersOnlineEvent) {
			loguj("Nowy user online: " + username);
			// dodanie do kolejki rysujących
			drawingQueue.add(username);

			// dorzucenie użytkownika do rankingu z 0 pkt
			localModel.getUserRanking().addNewUser(username);
			// wysłanie mu całego rankingu
			loguj("wysyłamy do " + username + " taki ranking: "
					+ localModel.getUserRanking().getUsersOnline());
			server.sendEvent(username, new ResetUserRankingEvent(localModel
					.getUserRanking().getFullRanking()));

			// wysyłanie użytkownikowi całego rysunku
			Event wholeDrawingEvent = new NewPointsDrawnEvent(localModel
					.getDrawingModel().getDrawing());
			server.sendEvent(username, wholeDrawingEvent);

			server.broadcastEvent(event);

			// TODO usunąć jak dodamy jakiś start!
			if (!gameStared && drawingQueue.size() > 1) {
				loguj("Startujemy grę, bo mamy dwóch użytkowników");
				reactTo("", new NewGameEvent());
			}

			// wysyłanie mu informacji o aktualnej rundzie
			server.sendEvent(
					username,
					new NextRoundStartsEvent(gameStared ? drawingQueue.peek()
							: "none", roundTimer.getTimeLeft(), roundTimer
							.getRoundTime()));

			return;
			// rzucenie wyjątku, jeśli użytkownik już jest zalogowany? - być
			// może, jeśli chcecie, to nawet może być assert
		}

		if (event instanceof UsersOfflineEvent) {
			loguj(username + " stał się offline");
			// jeśli zniknął użytkownik właśnie rysujacy
			if (username.equals(drawingQueue.peek())) {
				drawingQueue.poll();
				roundTimer.stopTimer();
				server.broadcastEvent(new MessageSendEvent(CHAT_SERVER_NAME,
						"Current drawing user left game, skiping to next round."));

				/*
				 * TODO FROM TESTS: w obecnym kształcie mamy drobną sprzeczność:
				 * NIE zaczynamy gry, dopóki nie pojawi się drugi zawodnik ale
				 * ZACZYNAMY nową rundę, gdy wszyscy prócz jednego zawodnika
				 * przejdą w stan offline
				 */

				if (!drawingQueue.isEmpty()) {
					startNextRound();
				} else {
					loguj("Brak nam użyszkodników :(");
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

		if (event instanceof NewMessageWrittenEvent) {
			NewMessageWrittenEvent castedEvent = (NewMessageWrittenEvent) event;
			/*
			 * nie przyjmujemy wiadomości od użytkownika, który rysuje - jeśli
			 * on chciał coś napisać, informacja, że mu nie wolno.
			 */
			if (drawingQueue != null && username.equals(drawingQueue.peek())) {
				server.sendEvent(username, new MessageSendEvent(
						CHAT_SERVER_NAME,
						"You CAN'T write on chat while drawin'!"));
				loguj(username + " próbował napisać, choć aktualnie rysuje");
				return;
			}
			/*
			 * update lokalnego modelu czatu. update czatu (i nie tylko) u
			 * wszystkich
			 */
			MessageSendEvent messageEvent = new MessageSendEvent(username,
					castedEvent.getMessage());
			localModel.getChatMessagesList().reactTo(messageEvent);

			server.broadcastEvent(messageEvent);

			/*
			 * jeśli użytkownik zgadł (nie patrzymy na wielkość liter i białe
			 * znaki na końcu i początku hasła) i trwa aktualnie sesja rysowania
			 */
			String newMessage = castedEvent.getMessage().trim().toLowerCase();
			String currentWord = nowBeingDrawnWord.trim().toLowerCase();
			if (someoneIsDrawing && newMessage.equals(currentWord)) {
				roundTimer.stopTimer();
				localModel.getUserRanking().addPointsToUser(username,
						getPointsForGuessing());
				String drawingUser = switchUserFromTop();
				localModel.getUserRanking().addPointsToUser(drawingUser,
						getPointsForDrawing());
				server.broadcastEvent(new WordGuessedEvent(nowBeingDrawnWord,
						username, getPointsForGuessing(), drawingUser,
						getPointsForDrawing()));
				startNextRound();
				return;
			}
			// później - sprawdź, czy to nie koniec gry

			/*
			 * sprawdzanie czy słowa są podobne
			 */
			if (areSimilar(newMessage, currentWord)) {
				server.sendEvent(username, new MessageSendEvent(
						CHAT_SERVER_NAME, "You nearly guessed!"));
			}

			return;
		}

		if (event instanceof NewPointsDrawnEvent) {
			/*
			 * nowe punkty - jeśli przyszły od właściwej osoby, update'uj nasz
			 * model i wyślij info do wszystkich SEEMS DONE X
			 */

			// nie jesteśmy w trakcie sesji rysowania
			if (!someoneIsDrawing || drawingQueue.isEmpty()) {
				loguj("nie jesteśmy w trakcie sesji rysowania");
				return;
			}

			// bo osoba była zła, a zupa za słona
			if (!username.equals(drawingQueue.peek())) {
				loguj("bo osoba była zła, a zupa za słona");
				return;
			}

			localModel.getDrawingModel().actualiseDrawing(
					((NewPointsDrawnEvent) event).getPoints());
			server.broadcastEvent(event);
			return;
		}

		if (event instanceof ClearScreenEvent) {

			if (!username.equals(drawingQueue.peek())) {
				loguj("bo osoba była zła, a zupa za słona");
				return;
			}

			server.broadcastEvent(event);
		}
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
		if (!gameStared)
			return;

		String drawingUser = switchUserFromTop();
		if (!someoneIsDrawing) {
			server.sendEvent(drawingUser, new CloseWordInputEvent(drawingUser));
		}

		localModel.getUserRanking().addPointsToUser(drawingUser,
				getPointsForBadDrawing());
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
	 * Funkcja zwracająca liczbę punktów przysługującą graczowi za zgadnięcie
	 * hasła w danym momencie gry. Aktualnie czeka na rozszerzenie.
	 * 
	 * @return
	 */
	private int getPointsForGuessing() {
		return 5;
	}

	/**
	 * Funkcja zwracająca liczbę punktów przysługującą graczowi za narysowanie
	 * (gdy ktoś odgadł, co zostało narysowane). Aktualnie czeka na
	 * rozszerzenie.
	 * 
	 * @return
	 */
	private int getPointsForDrawing() {
		return 10;
	}

	/**
	 * Funkcja zwracająca liczbę punktów przysługującą graczowi za narysowanie
	 * gdy nikt nie odgadł, a czas dobiegł końca. Aktualnie czeka na
	 * rozszerzenie.
	 * 
	 * @return
	 */
	private int getPointsForBadDrawing() {
		return -5;
	}

	/**
	 * Funkcja pomocnicza, do startowania następnej rundy, z graczem aktualnie u
	 * góry kolejki jako rysującym. Czyści planszę, prosi o hasło do rysowania.
	 */
	private void startNextRound() {
		// wyczyszczenie planszy
		localModel.getDrawingModel().clearScreen();
		server.broadcastEvent(new ClearScreenEvent());

		// rozpoczyna odliczanie czasu
		roundTimer.startRound();

		// rozpoczęcie nowej rundy w rankingu
		localModel.getUserRanking().nextRound(drawingQueue.peek());
		server.broadcastEvent(new NextRoundStartsEvent(drawingQueue.peek(),
				roundTimer.getRoundTime(), roundTimer.getRoundTime()));

		// wysyła użytkownikowi pytanie o hasło do rysowania
		server.sendEvent(drawingQueue.peek(), new NewWordIsNeededEvent());
		someoneIsDrawing = false;
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

	// do wypisywania logów
	private void loguj(String str) {
		System.out.println("GameLogic: " + str);
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
