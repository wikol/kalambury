package pl.uj.edu.tcs.kalambury_maven.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import morfologik.stemming.PolishStemmer;
import morfologik.stemming.WordData;

public class DictionaryAdapter {
	List<String> words = new ArrayList<>();
	Random generator = new Random();

	public DictionaryAdapter(String wordGroup) {
		PolishStemmer stemmer = new PolishStemmer();
		Iterator<WordData> stemmerIterator = stemmer.iterator();
		while (stemmerIterator.hasNext()) {
			WordData word = stemmerIterator.next();
			if (word.getTag().toString().contains(wordGroup)
					&& !Character.isUpperCase(word.getWord().charAt(0))) {
				words.add(word.getWord().toString());
			}
		}
	}

	public String nextWord() {
		return words.get(generator.nextInt(words.size()));
	}
}