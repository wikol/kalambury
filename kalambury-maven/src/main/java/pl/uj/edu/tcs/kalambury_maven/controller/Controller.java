package pl.uj.edu.tcs.kalambury_maven.controller;

import pl.uj.edu.tcs.kalambury_maven.event.Event;
import pl.uj.edu.tcs.kalambury_maven.event.EventNotHandledException;
import pl.uj.edu.tcs.kalambury_maven.model.Model;
import pl.uj.edu.tcs.kalambury_maven.network.Network;
import pl.uj.edu.tcs.kalambury_maven.view.View;

/**
 * Interfejs do implementowania przez kontroler
 * Wszystkie metody powinny być dosyć jasne po przeczytaniu opisu interfejsu View
 * @author Wiktor Kuropatwa
 *
 */
public interface Controller {
	public void reactTo(Event e) throws EventNotHandledException;
	public void setView(View v);
	public View getView();
	public void setModel(Model m);
	public Model getModel();
	public void setNetwork(Network n);
	public Network getNetwork();
}
