package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Ponieważ przykład wart jest więcej niż 1000 słów
 * @author Wiktor Kuropatwa
 *
 */
class DummyEvent implements Event {
	private static final long serialVersionUID = 5627949814462444226L;
	private String msg;
	public DummyEvent(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
}
class DummyEventHandler implements EventHandler {
	@Override
	public void handle(Event e) {
		System.out.println(((DummyEvent) e).getMsg());
	}
}
public class EventReactorExample {
	static EventReactor r = new EventReactor();
	public static void main(String[] args) {
		try {
			r.handle(new DummyEvent("Will fail"));
		} catch(EventNotHandledException e) {
			System.out.println("Failed as expected");
		}
		r.setHandler(DummyEvent.class, new DummyEventHandler());
		try {
			r.handle(new DummyEvent("I'm a dummy event!"));
		} catch(EventNotHandledException e) {
			System.out.println("This one shouldn't fail");
		}
	}
}
