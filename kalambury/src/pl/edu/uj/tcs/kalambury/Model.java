package pl.edu.uj.tcs.kalambury;

/**
 * Interfejs do implementowania przez model
 * Wszystkie metody powinny być dosyć jasne po przeczytaniu opisu interfejsu View	
 * @author Wiktor Kuropatwa
 *
 */
public interface Model {
	public void reactTo(Event e) throws EventNotHandledException;
	public void setController(Controller c);
	/**
	 * Rejestruje dany view jako nasłuchujący zmian w modelu, w przypadku każdej
	 * zmiany, model powinien poinformować wszystkie nasłuchujące go view za
	 * pomocą odpowiedniego eventu
	 * @param v view, który ma zostać zarejestrowany
	 */
	public void registerView(View v);
}
