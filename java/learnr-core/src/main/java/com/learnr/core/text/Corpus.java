package com.learnr.core.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.util.Verify;

public class Corpus<I> {

	private static final Logger logger = LoggerFactory.getLogger(Corpus.class);

	private final Map<I, String> corpus;
	private final int size;

	private List<TermFreqVector<I>> termVectors = new ArrayList<TermFreqVector<I>>();
	
	private Set<String> vocabulary = new HashSet<String>();
	private Set<String> corpusStopWords = new HashSet<String>();
	
	private List<String> dimensionVector = new ArrayList<String>();
	

	/* --- Constructors --- */

	public Corpus(Map<I, String> inCorpus) {
		Verify.notEmpty(inCorpus);

		this.corpus = inCorpus;
		this.size = inCorpus.size();

		logger.info("Text corpus size : " + size);
	}

	/* --- Methods --- */

	public void process() {

		String origText;
		TermFreqVector<I> tfVector;
		Map<String, Integer> tf;

		for (I i : corpus.keySet()) {
			origText = corpus.get(i);
			tfVector = new TermFreqVector<I>(i, origText);

			// process text frequency and update vocabulary 
			tf = tfVector.getTermFrequency();
			vocabulary.addAll(tf.keySet());
			
			// Add to the list
			termVectors.add(tfVector);
		}
		
		// Note : dimension vector = vocabulary - ( standard stopwords + corpus stopwords )

		// Corpus stop words
		corpusStopWords = StopWords.findStopWordsFromTermVectors(termVectors);
		
		// whole stop word set
		Set<String> stopWords = new HashSet<String>();
		stopWords.addAll(corpusStopWords);
		stopWords.addAll(StopWords.STANDARD_STOP_WORDS);
		
		// Final dimension vector
		dimensionVector.addAll(vocabulary);
		dimensionVector.removeAll(stopWords);
		
		// prepare term Freq vectors with dimension vector
		for (TermFreqVector<I> tfv : termVectors) {
			tfv.updateTermDimensionVector(dimensionVector);
		}

	}

	/* --- Getters and Setters --- */

	public int getSize() {
		return size;
	}

	public List<TermFreqVector<I>> getTermVectors() {
		return Collections.unmodifiableList(termVectors);
	}

	public Set<String> getVocabulary() {
		return Collections.unmodifiableSet(vocabulary);
	}

	public Set<String> getCorpusStopWords() {
		return Collections.unmodifiableSet(corpusStopWords);
	}

	public List<String> getDimensionVector() {
		return Collections.unmodifiableList(dimensionVector);
	}
	
}
