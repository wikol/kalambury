package pl.uj.edu.tcs.kalambury_maven.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import morfologik.stemming.PolishStemmer;
import morfologik.stemming.WordData;

public class DictionaryAndBookAdapter {
	private static List<String> readBook(String filename) {
		FileInputStream fr = null;
		try {
			fr = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InputStreamReader ir = null;
		try {
			ir = new InputStreamReader(fr, "cp1250");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Scanner sc = new Scanner(ir);
		List<String> words = new LinkedList<>();
		while (sc.hasNext()) {
			String next = sc.next();
			words.add(next);
		}
		return words;
	}

	private static Set<String> filter(List<String> words,
			PolishStemmer dictionary) {
		Set<String> filtered = new TreeSet<>();
		for (String word : words) {
			for (Object w : dictionary.lookup(word)) {
				WordData wdata = (WordData) w;
				filtered.add(wdata.getStem().toString());
			}
		}
		return filtered;
	}

	private static List<String> getProperWords(Set<String> filtered,
			PolishStemmer dictionary, String wordGroup) {
		List<String> toRet = new ArrayList<>();
		for (String word : filtered) {
			WordData wdata = (WordData) dictionary.lookup(word).get(0);
			if (!Character.isUpperCase(word.charAt(0))
					&& wdata.getTag().toString().contains(wordGroup))
				toRet.add(word);
		}
		return toRet;
	}

	private List<String> properWords;
	private Random rG;

	public DictionaryAndBookAdapter(String filename, String wordgroup) {
		PolishStemmer dict = new PolishStemmer();
		properWords = getProperWords(filter(readBook(filename), dict), dict,
				wordgroup);
		rG = new Random();
	}

	public static void main(String[] args) {
		DictionaryAndBookAdapter ada = new DictionaryAndBookAdapter(
				"/home/wikol/Pobrane/682.txt", "subst:sg:nom:m");
	}
	
	public String nextWord() {
		return properWords.get(rG.nextInt(properWords.size()));
	}
}
