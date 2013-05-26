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
	private boolean gameStared = false;


	public synchronized void reactTo(String username, Event event) {

		loguj("Otrzymano " + event.toString());
		if (event instanceof NewGameEvent) {
			// myślę, że oni będą po prostu dorzucani do kolejki od razu jak się
			// zonlineują
			// for (String name : localModel.getUserRanking().getUsersOnline())
			// drawingQueue.add(name);
			startNextRound();
			gameStared = true;
			return;
		}

		if (event instanceof NewWordForGuessingEvent) {
			loguj("nowe hasełko otrzymano od "+username);
			// jeśli hasło wysłał nam nie ten kto teraz ma rysować to ignorujemy
			if (!username.equals(drawingQueue.peek()))
				return;
			loguj("no dobra, od ciebie nas ono interesuje");

			nowBeingDrawnWord = ((NewWordForGuessingEvent) event).getWord();
			someoneIsDrawing = true;
			server.sendEvent(drawingQueue.peek(), new StartDrawingEvent(drawingQueue.peek()));
			return;
		}

		if (event instanceof UsersOnlineEvent) {
			loguj("Nowy user online: "+username);
			// dodanie do kolejki rysujących
			drawingQueue.add(username);

			//wysłanie mu całego rankingu
			loguj("wysyłamy do "+username+" taki ranking: "+localModel.getUserRanking().getUsersOnline());
			server.sendEvent(username, new UsersOnlineEvent(localModel
					.getUserRanking().getUsersOnline()));
			// dorzucenie użytkownika do rankingu z 0 pkt
			localModel.getUserRanking().addNewUser(username);

			// wysyłanie użytkownikowi całego rysunku
			Event wholeDrawingEvent = new NewPointsDrawnEvent(localModel
					.getDrawingModel().getDrawing());
			server.sendEvent(username, wholeDrawingEvent);

			server.broadcastEvent(event);

			// TODO usunąć jak dodamy jakiś start!
			if (!gameStared && drawingQueue.size() > 2) {
				reactTo("", new NewGameEvent());
			}
			return;
			// rzucenie wyjątku, jeśli użytkownik już jest zalogowany? - być
			// może, jeśli chcecie, to nawet może być assert
		}

		if (event instanceof UsersOfflineEvent) {
			loguj(username+" stał się offline");
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
			return;
		}

		if (event instanceof NewMessageWrittenEvent) {
			NewMessageWrittenEvent castedEvent = (NewMessageWrittenEvent) event;
			/*
			 * nie przyjmujemy wiadomości od użytkownika, który rysuje - jeśli
			 * on chciał coś napisać, informacja, że mu nie wolno.
			 */
			if (drawingQueue != null
					&& username.equals(drawingQueue.peek())) {
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
				localModel.getUserRanking().addPointsToUser(
						username, getPointsForGuessing());
				localModel.getUserRanking().addPointsToUser(
						drawingQueue.peek(), getPointsForDrawing());
				drawingQueue.add(drawingQueue.poll());
				server.broadcastEvent(new WordGuessedEvent(nowBeingDrawnWord,
						username));
				startNextRound();
			}
			// później - sprawdź, czy to nie koniec gry
			return;
		}

		if (event instanceof NewPointsDrawnEvent) {
			loguj("Wchodzimy do NewPointsDrawnEvent: "+username);
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
			loguj("Wychodzimy z NewPointsDrawnEvent");
			return;
		}
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
	 * Funkcja pomocnicza, do startowania następnej rundy. Czyści planszę, prosi
	 * o hasło do rysowania.
	 */
	private void startNextRound() {
		// wyczyszczenie planszy
		localModel.getDrawingModel().clearScreen();
		server.broadcastEvent(new ClearScreenEvent());
		
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

}
