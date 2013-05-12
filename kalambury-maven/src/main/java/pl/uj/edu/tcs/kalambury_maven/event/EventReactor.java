package pl.uj.edu.tcs.kalambury_maven.event;

import java.util.HashMap;
import java.util.Map;
/**
 * Klasa, której będziemy używać do decydowania, jaki kod ma zostać wykonany
 * w przypadku otrzymania przez moduł określonego eventu.
 * @author Wiktor Kuropatwa
 *
 */
public class EventReactor {
	private Map<Class<?>, EventHandler> handlers = new HashMap<>();
	/**
	 * Ustawia kod, który ma zostać wykonany w przypadku wystąpienia określonej klasy eventu
	 * @param eventClass obiekt Class dla klasy eventów, która ma być obsługiwana przez dany handler
	 * @param handler rzeczony handler
	 */
	public <T extends Event> void setHandler(Class<T> eventClass, EventHandler handler) {
		handlers.put(eventClass, handler);
	}
	/**
	 * Wywołuje odpowiedni dla zaistniałego eventu handler, jeżeli takowy nie istnieje, rzuca wyjątkiem
	 * @param e zaistniały event
	 * @throws EventNotHandledException w przypadku, gdy dana klasa eventów nie ma przypisanego handlera
	 */
	public void handle(Event e) throws EventNotHandledException {
		if(!handlers.containsKey(e.getClass()))
			throw new EventNotHandledException();
		handlers.get(e.getClass()).handle(e);
	}
}
