package com.learnr.core.text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextProcessor {

	private static final Logger logger = LoggerFactory.getLogger(TextProcessor.class);
	
	public static Map<String, Integer> getTermFrequency(String inText) {
		if(inText == null || inText.isEmpty())
			return null;
		
		Map<String, Integer> tfMap = new HashMap<String, Integer>();
		
		// convert to lowercase
		String text = inText.toLowerCase();
		
		// Get tokens (lemmatized)
		List<String> tokens = Lemmatizer.lemmatize(text);
		logger.debug("Lemmatized tokens : " + tokens);
		
		for (String term : tokens) {
			int count = 1;
			if(tfMap.containsKey(term))
				count = tfMap.get(term) + 1;
			
			tfMap.put(term, count);
		}
		
		return tfMap;
	}
	
	
	public static Set<String> getDistinctLemmas(String inText) {
		if(inText == null || inText.isEmpty())
			return null;
		
		// convert to lowercase
		String text = inText.toLowerCase();
		
		// Get tokens (lemmatized)
		List<String> tokens = Lemmatizer.lemmatize(text);
		if(tokens != null) 
			return new HashSet<String>(tokens);
		
		return null;
	}
	

	public static Set<String> getDistinctLemmas(List<String> inTextList) {
		if(inTextList == null)
			return null;
		
		Set<String> distinctWords = new HashSet<String>();
		for (String inText : inTextList) {
			distinctWords.addAll(getDistinctLemmas(inText));
		}
		
		return distinctWords;
	}
	
}
