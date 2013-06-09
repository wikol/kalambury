package pl.uj.edu.tcs.kalambury_maven.server;

public class RiddlesGeneratorLevelHard implements RiddlesGenerator {

	DictionaryAdapter nouns = new DictionaryAdapter("subst:sg:nom:m");
	DictionaryAdapter adjs = new DictionaryAdapter("adj:sg:nom.voc:m");
	public RiddlesGeneratorLevelHard() {
	}
	private String currentRiddle;
	@Override
	public String getCurrentRiddle() {
		return currentRiddle;
	}

	@Override
	public String nextRiddle() {
		currentRiddle = adjs.nextWord() + " " + nouns.nextWord();
		return currentRiddle;
	}

}
