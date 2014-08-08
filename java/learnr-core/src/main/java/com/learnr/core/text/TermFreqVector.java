package com.learnr.core.text;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.Clusterable;

import com.learnr.core.exception.LearnRException;
import com.learnr.util.Verify;

public final class TermFreqVector<I> implements Clusterable {

	private final I id;
	private final String originalText;

	private final Map<String, Integer> termFrequency;
	
	private final int wordCount;
	private final int distinctWordCount;
	
	private double[] point = null;
	
	/* --- Constructors --- */
	
	public TermFreqVector(I id, String originalText) {
		super();
		Verify.notNull(id, originalText);
		
		this.id = id;
		this.originalText = originalText;
		this.termFrequency = TextProcessor.getTermFrequency(originalText);
		
		// word counts
		this.wordCount = calculateWordCount();
		this.distinctWordCount = this.termFrequency.keySet().size();
	}
	
	public TermFreqVector(I id, Map<String, Integer> termFrequency) {
		super();
		Verify.notNull(id, termFrequency);
		
		this.id = id;
		this.originalText = null;
		this.termFrequency = termFrequency;
		
		// word counts
		this.wordCount = calculateWordCount();
		this.distinctWordCount = this.termFrequency.keySet().size();
	}
	
	/* --- Private Helpers --- */
	
	private final int calculateWordCount() {
		int wCount = 0;
		List<Integer> counts = (List<Integer>) termFrequency.values();
		for (Integer count : counts) {
			if(count != null)
				wCount = wCount + count;
		}
		
		return wCount;
	}

	/* --- Getters and Setters --- */
	
	public I getId() {
		return id;
	}

	public String getOriginalText() {
		if(originalText == null)
			throw new LearnRException("Object was initialized with no original text.");
			
		return originalText;
	}

	public int getWordCount() {
		return wordCount;
	}

	public int getDistinctWordCount() {
		return distinctWordCount;
	}

	public Map<String, Integer> getTermFrequency() {
		return Collections.unmodifiableMap(termFrequency);
	}
	
	/* --- Other Methods --- */
	
	public void updateTermDimensionVector(List<String> dimensionVector) {
		Verify.notEmpty(dimensionVector);
		
		int length = dimensionVector.size();
		point = new double[length];
		
		// Update the vector as per dimension vector
		
		String term;
		Integer freq;
		for (int i = 0; i < dimensionVector.size(); i++) {
			term = dimensionVector.get(i);
			freq = termFrequency.get(term);
			
			point[i] = (freq == null) ? 0 : freq;
		}
		
	}

	
	/* --- Clustering related --- */
	
	@Override
	public double[] getPoint() {
		if(point == null)
			throw new LearnRException("Object is not yet processed term dimension vector");

		return point;
	}
	
	
}
