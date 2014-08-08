package com.learnr.core.text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.util.Verify;

public class StopWords {
	
	private static final Logger logger = LoggerFactory.getLogger(StopWords.class);
	
	
	public static final Set<String> STANDARD_STOP_WORDS;
	
	static {
		// TODO finish this
		STANDARD_STOP_WORDS = new HashSet<String>();
	}
	
	public static Set<String> findStopWords(List<String> inStrs) {
		Verify.notEmpty(inStrs);
		
		List<TermFreqVector<Integer>> tfVectors = new ArrayList<TermFreqVector<Integer>>();
		for (int i = 0; i < inStrs.size(); i++) {
			tfVectors.add(new TermFreqVector<Integer>(i, inStrs.get(i)));
		}
		
		return findStopWordsFromTermVectors(tfVectors);
	}
	
	public static <I> Set<String> findStopWordsFromTermVectors(List<TermFreqVector<I>> tfVectors) {
		Verify.notEmpty(tfVectors);
		
		
		// TODO finish
		
				
		
		
		return null;
	}
	
}
