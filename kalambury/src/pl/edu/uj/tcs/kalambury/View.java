package pl.edu.uj.tcs.kalambury;


/**
 * Interfejs, który będą implementowały wszystkie nasze View.
 * 
 * Uwaga: klasa implementująca View powinna posiadać wszystkie swoje
 * kompontenty za pomocą kompozycji i przekazywać do nich eventy
 * pochodzące z reactTo za pomocą EventReactora
 * @author Wiktor Kuropatwa
 *
 */
public interface View {
	/**
	 * Powinno wykonywać kod odpowiedzialny za reakcję View na zaistniały event
	 * (wykorzystując EventReactor i przekazując event do odpowiedniego komponentu)
	 * @param e event do zareagowania na
	 * @throws EventNotHandledException rzucany w przypadku, gdy nasz View nie umie
	 * reagować na eventy tego typu
	 */
	public void reactTo(Event e) throws EventNotHandledException;
	/**
	 * Ustawia kontroler dla danego View, wszystkie eventy wynikłe z interakcji użytownika
	 * z GUI powinny być wysyłane do tego właśnie kontrolera
	 * @param c rzeczony kontroler
	 */
	public void setController(Controller c);
	/**
	 * Informuje View o tym, że powinien nasłuchiwać zmian w modelu m i zmieniać wyświetlane
	 * przez siebie informacje wraz ze zmianami w modelu - setModel powinno zarejestrować
	 * się w modelu przez metodę registerView
	 * @param m rzeczony model
	 */
	public void setModel(Model m);
}
