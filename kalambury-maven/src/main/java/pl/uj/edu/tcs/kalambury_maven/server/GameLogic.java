package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.NewGameEvent;
import pl.uj.edu.tcs.kalambury_maven.event.MessageSendEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewMessageWrittenEvent;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;
import pl.uj.edu.tcs.kalambury_maven.model.ChatMessagesList;
import pl.uj.edu.tcs.kalambury_maven.model.DrawingModel;
import pl.uj.edu.tcs.kalambury_maven.model.SimpleModel;
import pl.uj.edu.tcs.kalambury_maven.model.UserRanking;
import pl.uj.edu.tcs.kalambury_maven.view.Ranking;

public class GameLogic {

	private Server server;
	private Queue<String> drawingQueue; // kolejka rysujących - aktualnie
										// rysujący: drawingQueue.peek()
	private SimpleModel localModel = new SimpleModel();

	private String nowBeingDrawnWord; // aktualne hasło

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

	public synchronized void reactTo(String username, Event event) {

		/*
		 * w przypadku eventu mówiącego, że ktoś jest online/offline - czy mamy
		 * reagować tworzeniem mu modelu, view i controllera, czy to fakt
		 * pojawienia się tych obiektów wywołuje nasz event??? - macie reagować
		 * wysłaniem mu wszystkich danych potrzebnych do utworzenia modelu
		 * aktualnej rozgrywki - samym tworzeniem zajmuje się część klienta
		 * pojawienia się tych obiektów wywołuje nasz event???
		 * 
		 * To jest pytanie :P na które jak umiesz, to proszę odpowiedz ;) Tak
		 * jak i na inne pytania.
		 */
		if(event instanceof NewGameEvent) {
			for(String name : localModel.getUserRanking().getUsersOnline())
				drawingQueue.add(name);
		}
		if (event instanceof UsersOnlineEvent) { // to be changed into
													// UserOnlineEvent !!!
			// dodanie do kolejki rysujących
			drawingQueue.add(username);
			
		
			server.broadcastEvent(event);
			// TODO
			// update rankingu głównego - dodanie tego użytkownika z 0 pkt
			// wysłanie do wszystkich update'u rankingu
			// wypisanie informacji na czacie wysyłane do wszystkich
			// użytkowników

			/*
			 * Punkty "wysłanie do czatu" i "update rankingu" to jeden punkt -
			 * wystarczy poinformować klientów, że nowy gracz się
			 * pojawił/zniknął - lokalny ranking sam na to zareaguje
			 */

			// rzucenie wyjątku, jeśli użytkownik już jest zalogowany? - być
			// może, jeśli chcecie, to nawet może być assert
		}
		if (event instanceof UsersOfflineEvent) { // to be changed into
													// UserOfflineEvent !!!
			// usunięcie z kolejki rysujących
			drawingQueue.remove(username);
			
			server.broadcastEvent(event);
			// TODO
			// update rankingu głównego - usunięcie tego użytkownika z rankingu
			// jeśli ten użytkownik właśnie rysował - zakończenie gry jak w
			// przypadku zgadnięcia hasła
			// ale z komunikatem "użytkownik zniknął" - rozesłane do wszystkich
			// rozesłanie do wszystkich użytkowników update'u rankingu
			// informacja na czacie dla wszystkich

			/*
			 * Punkty "wysłanie do czatu" i "update rankingu" to jeden punkt -
			 * wystarczy poinformować klientów, że nowy gracz się
			 * pojawił/zniknął - lokalny ranking sam na to zareaguje
			 */

			// rzucenie wyjątku, jeśli takiego użytkownika nie było? - j.w.
		}

		if (event instanceof NewMessageWrittenEvent) {
			NewMessageWrittenEvent castedEvent = (NewMessageWrittenEvent) event;
			/*
			 * nie przyjmujemy wiadomości od użytkownika, który rysuje - jeśli
			 * on chciał coś napisać, informacja, że mu nie wolno.
			 */
			if (castedEvent.getUser() == drawingQueue.peek()) {
				server.sendEvent(castedEvent.getUser(),
						new NewMessageWrittenEvent("!!!_SERVER",
								"You CAN'T write on chat while drawin'!"));
			} else {
				/*
				 * update lokalnego modelu czatu. update czatu u wszystkich
				 */
				localModel.getChatMessagesList().reactTo(event);
				server.broadcastEvent(event);
				/*
				 * jeśli użytkownik zgadł (nie patrzymy na wielkość liter)
				 */
				if (castedEvent.getMessage()
						.equalsIgnoreCase(nowBeingDrawnWord)) {
					server.broadcastEvent(new WordGuessedEvent(nowBeingDrawnWord, castedEvent.getUser()));
					localModel.getUserRanking().addPointsToUser(
							castedEvent.getUser(), getPointsForGuessing());
					localModel.getUserRanking().addPointsToUser(
							drawingQueue.peek(), getPointsForDrawing());
					drawingQueue.add(drawingQueue.poll());
					localModel.getUserRanking().nextRound(drawingQueue.peek());
				}
			} // później - sprawdź, czy to nie koniec gry
			return;
		}
		if (event instanceof NewPointsDrawnEvent) {
			/*
			 * nowe punkty - jeśli przyszły od właściwej osoby, update'uj nasz
			 * model i wyślij info do wszystkich SEEMS DONE X
			 */
			if (username == drawingQueue.peek()) {
				localModel.getDrawingModel().actualiseDrawing(
						((NewPointsDrawnEvent) event).getPoints());
				server.broadcastEvent(event);
			}
			return;
		}
	}

	public void setServer(Server s) {
		server = s;
	}

}
