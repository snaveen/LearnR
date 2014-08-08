package com.learnr.core.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.util.Verify;

public class TextProcessor {

	private static final Logger logger = LoggerFactory.getLogger(TextProcessor.class);

	private final Map<String, String> corpus;
	private final int corpusSize;

	private List<String> dimensionVector;
	private Map<String, List<String>> mapOfLemman;
	private Map<String, Map<String, Integer>> wordFrequency;
	private Map<String, Map<String, Integer>> wordFrequencyWithOutStopWords;
	private List<String> keys;
	private List<String> stopWords;
	StopWordUtil swUtil = new StopWordUtil();

	public TextProcessor(Map<String, String> inCorpus) {
		Verify.notNull(inCorpus);

		this.corpus = inCorpus;
		this.corpusSize = inCorpus.size();

		logger.info("Text corpus size : " + corpusSize);
	}

	/* --- Methods --- */

	public void process() {
		mapOfLemman = lemmatizeCorpus();
		wordFrequency = generateWordCount();
		keys = new ArrayList<String>(corpus.keySet());
		wordFrequencyWithOutStopWords = generateWordCountWithoutStopWords();
		stopWords = generateStopWords();
		dimensionVector = generatingDimension();
	}

	// Lemmatize corpus

	private Map<String, List<String>> lemmatizeCorpus() {
		Lemmatizer lemma = new Lemmatizer();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < corpusSize; i++) {
			map.put(keys.get(i), lemma.lemmatize(corpus.get(keys.get(i))));
		}
		return map;
	}

	// getWordcount of corpus
	public Map<String, Map<String, Integer>> generateWordCount() {
		Map<String, Map<String, Integer>> wordCount = new HashMap<String, Map<String, Integer>>();
		for (int i = 0; i < corpusSize; i++) {
			wordCount.put(keys.get(i), swUtil.getWordCount(mapOfLemman.get(keys.get(i))));
		}
		return wordCount;
	}

	public Map<String, Map<String, Integer>> generateWordCountWithoutStopWords() {
		Map<String, Map<String, Integer>> map = generateWordCount();
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		list.addAll(swUtil.generateWordCount((List<Map<String, Integer>>) map.values(), stopWords));
		for (int i = 0; i < corpusSize; i++) {
			map.put(keys.get(i), list.get(i));
		}
		return map;
	}

	// TODO Get StopWords
	public List<String> generateStopWords() {
		List<String> stopWords = new ArrayList<String>();
		List<Map<String, Integer>> values = new ArrayList<Map<String, Integer>>(wordFrequency.values());
		stopWords.addAll(swUtil.stopWords(values));
		return stopWords;
	}

	// TODO get TermVectors
	public List<TermFreVector> getTermFrequencyVectors() {
		return (List<TermFreVector>) generateVectorsAsMap().values();
	}

	// TODO get TermVectorMap
	public Map<String, TermFreVector> generateVectorsAsMap() {
		GeneratingVectors vectors = new GeneratingVectors();
		Map<String, TermFreVector> map = new HashMap<String, TermFreVector>();
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		list.addAll(vectors.dimensionVector((List<Map<String, Integer>>) wordFrequencyWithOutStopWords.values()));
		for (int i = 0; i < corpusSize; i++) {
			map.put(keys.get(i), (TermFreVector) list.get(i));
		}
		return map;
	}

	public List<String> generatingDimension() {
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>(wordFrequencyWithOutStopWords.values());
		List<String> strings = new ArrayList<String>(list.get(0).keySet());

		for (int i = 1; i < corpusSize; i++) {
			strings.addAll(list.get(i).keySet());
		}
		return strings;
	}

	/* --- Getters and Setters --- */

	public Map<String, String> getCorpus() {
		return Collections.unmodifiableMap(corpus);
	}

	public Integer getCorpusSize() {
		return corpusSize;
	}

	public List<String> getDimensionVector() {
		return Collections.unmodifiableList(dimensionVector);
	}

	public List<String> getStopWords() {
		return Collections.unmodifiableList(stopWords);
	}
	public Map<String, Map<String, Integer>> getWordFrequency(){
		return Collections.unmodifiableMap( wordFrequencyWithOutStopWords);
	}
	public List<String> getIdOfCorpus(){
		return Collections.unmodifiableList(keys);
	}
}
