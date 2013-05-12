package pl.uj.edu.tcs.kalambury_maven.event;

import java.io.Serializable;


/**
 * Interfejs będący "sercem" naszego projektu - każde zdarzenie w programie
 * będzie polegało na wysłaniu obiektu implementującego event między odpowiednimi
 * klasami.
 * @author Wiktor Kuropatwa
 *
 */
public interface Event extends Serializable {
	
}
