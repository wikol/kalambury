package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.Random;

public class PilipiukGenerator implements RiddlesGenerator {
	
	DictionaryAndBookAdapter nounsM;
	DictionaryAndBookAdapter nounsF;
	Random rG = new Random();
	String currentRiddle;
	
	public PilipiukGenerator() {
		nounsM = new DictionaryAndBookAdapter("682.txt", "subst:sg:nom:m");
		nounsF = new DictionaryAndBookAdapter("682.txt", "subst:sg:nom:f");
	}
	@Override
	public String getCurrentRiddle() {
		return currentRiddle;
	}

	@Override
	public String nextRiddle() {
		if(rG.nextBoolean()) {
			currentRiddle = nounsM.nextWord();
		}
		else {
			currentRiddle = nounsF.nextWord();
		}
		return currentRiddle;
	}

}
