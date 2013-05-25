package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordForGuessingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewWordIsNeededEvent;
import pl.uj.edu.tcs.kalambury_maven.event.StartDrawingEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;

public class GameLogic {
	private static final String CHAT_SERVER_NAME = "!!!_SERVER";

	private Server server;
	private Queue<String> drawingQueue = new LinkedBlockingDeque<>();
	private SimpleModel localModel = new SimpleModel();

	private String nowBeingDrawnWord = ""; // aktualne hasło

	// true jeśli aktualnie ktoś rysuje i można zgadywać hasło
	private boolean someoneIsDrawing = false;

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
	 * Funkcja pomocnicza, do startowania następnej rundy. Czyści planszę, prosi
	 * o hasło do rysowania.
	 */
	private void startNextRound() {
		// wyczyszczenie planszy
		// TODO localModel.getDrawingModel().clearScreen();
		// rozpoczęcie nowej rundy w rankingu
		localModel.getUserRanking().nextRound(drawingQueue.peek());
		// wysyła użytkownikowi pytanie o hasło do rysowania
		server.sendEvent(drawingQueue.peek(), new NewWordIsNeededEvent());
		someoneIsDrawing = false;
	}

	// do wypisywania logów
	private void loguj(String str) {
		System.out.println("GameLogic: " + str);
	}

	public synchronized void reactTo(String username, Event event) {

		loguj("Otrzymano " + event.toString());
		if (event instanceof NewGameEvent) {
			// myślę, że oni będą po prostu dorzucani do kolejki od razu jak się
			// zonlineują
			// for (String name : localModel.getUserRanking().getUsersOnline())
			// drawingQueue.add(name);
			startNextRound();
			return;
		}

		if (event instanceof NewWordForGuessingEvent) {
			// jeśli hasło wysłał nam nie ten kto teraz ma rysować to ignorujemy
			if (!username.equals(drawingQueue.peek()))
				return;

			nowBeingDrawnWord = ((NewWordForGuessingEvent) event).getWord();
			someoneIsDrawing = true;
			server.broadcastEvent(new StartDrawingEvent(drawingQueue.peek()));
			return;
		}

		if (event instanceof UsersOnlineEvent) {
			// dodanie do kolejki rysujących
			drawingQueue.add(username);

			// dorzucenie użytkownika do rankingu z 0 pkt
			localModel.getUserRanking().addNewUser(username);

			// wysyłanie użytkownikowi całego rysunku
			Event wholeDrawingEvent = new NewPointsDrawnEvent(localModel
					.getDrawingModel().getDrawing());
			server.sendEvent(username, wholeDrawingEvent);

			server.broadcastEvent(event);
			return;
			// rzucenie wyjątku, jeśli użytkownik już jest zalogowany? - być
			// może, jeśli chcecie, to nawet może być assert
		}

		if (event instanceof UsersOfflineEvent) {
			// jeśli zniknął użytkownik właśnie rysujacy
			if (username.equals(drawingQueue.peek())) {
				drawingQueue.poll();
				server.broadcastEvent(new NewMessageWrittenEvent(
						CHAT_SERVER_NAME,
						"Current drawing user left game, skiping to next round."));

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
			// TODO
			// update rankingu głównego - usunięcie tego użytkownika z rankingu
			// jeśli ten użytkownik właśnie rysował - zakończenie gry jak w
			// przypadku zgadnięcia hasła
			// ale z komunikatem "użytkownik zniknął" - rozesłane do wszystkich
			// rozesłanie do wszystkich użytkowników update'u rankingu
			// informacja na czacie dla wszystkich

			// rzucenie wyjątku, jeśli takiego użytkownika nie było? - j.w.
			return;
		}

		if (event instanceof NewMessageWrittenEvent) {
			loguj("Wchodzimy do NewMessageWrittenEvent");
			NewMessageWrittenEvent castedEvent = (NewMessageWrittenEvent) event;
			/*
			 * nie przyjmujemy wiadomości od użytkownika, który rysuje - jeśli
			 * on chciał coś napisać, informacja, że mu nie wolno.
			 */
			if (drawingQueue != null && castedEvent.getUser().equals(drawingQueue.peek())) {
				server.sendEvent(castedEvent.getUser(),
						new MessageSendEvent(CHAT_SERVER_NAME,
								"You CAN'T write on chat while drawin'!"));
				loguj("nieprzyjmujemy od ciebie wiadomości");
				return;
			}
			loguj("to nie była chwila nieprzyjmowania");
			/*
			 * update lokalnego modelu czatu. update czatu (i nie tylko) u
			 * wszystkich
			 */
			MessageSendEvent messageEvent = new MessageSendEvent(username, castedEvent.getMessage());
			localModel.getChatMessagesList().reactTo(messageEvent);
			loguj("chat updatowany");
			server.broadcastEvent(messageEvent);

			/*
			 * jeśli użytkownik zgadł (nie patrzymy na wielkość liter i białe
			 * znaki na końcu i początku hasła) i trwa aktualnie sesja rysowania
			 */
			String newMessage = castedEvent.getMessage().trim().toLowerCase();
			String currentWord = nowBeingDrawnWord.trim().toLowerCase();
			if (someoneIsDrawing && newMessage.equals(currentWord)) {
				localModel.getUserRanking().addPointsToUser(
						castedEvent.getUser(), getPointsForGuessing());
				localModel.getUserRanking().addPointsToUser(
						drawingQueue.peek(), getPointsForDrawing());
				drawingQueue.add(drawingQueue.poll());
				server.broadcastEvent(new WordGuessedEvent(nowBeingDrawnWord,
						castedEvent.getUser()));
				startNextRound();
			}
			// później - sprawdź, czy to nie koniec gry
			loguj("Wychodzimy z NewMessageWrittenEvent");
			return;
		}

		if (event instanceof NewPointsDrawnEvent) {
			loguj("Wchodzimy do NewPointsDrawnEvent");
			/*
			 * nowe punkty - jeśli przyszły od właściwej osoby, update'uj nasz
			 * model i wyślij info do wszystkich SEEMS DONE X
			 */

			// nie jesteśmy w trakcie sesji rysowania
			if (!someoneIsDrawing)
				return;

			// bo osoba była zła, a zupa za słona
			if (!username.equals(drawingQueue.peek()))
				return;

			localModel.getDrawingModel().actualiseDrawing(
					((NewPointsDrawnEvent) event).getPoints());
			server.broadcastEvent(event);
			loguj("Wychodzimy z NewPointsDrawnEvent");
			return;
		}
	}

	public void setServer(Server s) {
		server = s;
	}

}
