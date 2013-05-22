package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Queue;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.NewPointsDrawnEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOfflineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.UsersOnlineEvent;
import pl.uj.edu.tcs.kalambury_maven.event.WordGuessedEvent;

public class GameLogic {

	private Server server;
	private Queue<String> drawingQueue; // kolejka rysujących - aktualnie
										// rysujący: drawingQueue.peek()

	// TODO
	// czy trzymać tu również listę zalogowanych użytkowników tak po prostu?
	// czy może zamiast tego korzystać z mapy (użytkownik->punkty) w klasie
	// UserRanking???
	/*
	 * Jeżeli takowa lista jest Wam potrzeb na to feel free, ale nie widzę na
	 * razie jakichś sensownych zastosowań dla takowej.
	 */

	public void reactTo(String username, Event event) {

		/*
		 * w przypadku eventu mówiącego, że ktoś jest online/offline - czy mamy
		 * reagować tworzeniem mu modelu, view i controllera, czy to fakt
		 * pojawienia się tych obiektów wywołuje nasz event??? - macie reagować
		 * wysłaniem mu wszystkich danych potrzebnych do utworzenia modelu
		 * aktualnej rozgrywki - samym tworzeniem zajmuje się część klienta
		 * 
		 * To jest pytanie :P na które jak umiesz, to proszę odpowiedz ;) Tak
		 * jak i na inne pytania.
		 */
		if (event instanceof UsersOnlineEvent) { // to be changed into
													// UserOnlineEvent !!!
			// TODO
			// dodanie do kolejki rysujących
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
			// TODO
			// usunięcie z kolejki rysujących
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
		if (event instanceof WordGuessedEvent) {
			// TODO
			// dodaj graczowi, który zgadł, punkty
			// dodaj graczowi, który rysował, punkty
			// później - sprawdź, czy to nie koniec gry
			// przerzuć gracza z początku kolejki na koniec
			// update'uj ranking
			// wyślij ten update do wszystkich
			// wypisz odpowiednie informacje na czacie u wszystkich

			/*
			 * Hm, to powinno być w NewMessageWrittenEvent pod ifem, czy to
			 * faktycznie jest hasło - WordGuessedEvent to Wy będziecie wysyłać!
			 */
		}
		if (event instanceof NewPointsDrawnEvent) {
			// jeśli nowe punkty pochodzą od tego, kto aktualnie rysuje
			if (username == drawingQueue.peek())
				server.broadcastEvent(event);
			// wpp. - nic ???
			/*
			 * Jeszcze updatujcie swój lokalny model rysunku - gdy nowy
			 * użytkownik się łączy, trzeba mu coś wysłać! Żeby wiedzieć co,
			 * skontaktujcie się z Piekasiami. Powinni Wam naklepać odpowiednie
			 * metody z DrawingModel
			 */
		}
	}

	public void setServer(Server s) {
		server = s;
	}

}
