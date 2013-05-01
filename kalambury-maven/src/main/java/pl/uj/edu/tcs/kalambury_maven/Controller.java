package pl.uj.edu.tcs.kalambury_maven;

/**
 * Interfejs do implementowania przez kontroler
 * Wszystkie metody powinny być dosyć jasne po przeczytaniu opisu interfejsu View
 * @author Wiktor Kuropatwa
 *
 */
public interface Controller {
	public void reactTo(Event e) throws EventNotHandledException;
	public void setView(View v);
	public void setModel(Model m);
}
