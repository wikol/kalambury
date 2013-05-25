package pl.uj.edu.tcs.kalambury_maven.event;

/**
 * Event do wysłania serwerowi gdy zostanie ustawione nowe słowo do zgadywania.
 * 
 * @author Anna Szybalska
 * 
 */
public class NewWordForGuessingEvent implements Event {
	private static final long serialVersionUID = 817678107192673008L;

	private String word;

	/**
	 * Konstruktor dla NewWordSetedEvent, przyjmuje hasło które zostało wybrane
	 * na hasło do zgadywania.
	 * 
	 * @param word
	 */
	public NewWordForGuessingEvent(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}
}
