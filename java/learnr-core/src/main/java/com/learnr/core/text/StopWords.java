package com.learnr.core.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.util.Verify;

public class StopWords {
	
	private static final Logger logger = LoggerFactory.getLogger(StopWords.class);
	
	private static final String STANDARD_STOPWORDS_FILE_PATH = "standard_stopwords.txt";
	public static final Set<String> STANDARD_STOPWORDS;

	/* --- Methods --- */
	
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
		Set<String> csw = new HashSet<String>();
		
		// TODO Finish this
		
		return csw;
	}
	
	
	/* --- Static Initializations --- */
	
	static {
		
		Set<String> stopwords = new HashSet<String>();
		
		File swf = new File(STANDARD_STOPWORDS_FILE_PATH);
		if(swf != null) {
			
			BufferedReader br;
			FileReader fr;
			try {
				fr = new FileReader(swf);
				br = new BufferedReader(fr);
	
				String line;
				while ((line = br.readLine()) != null) {
					stopwords.add(line.trim());
				}
	
				br.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		STANDARD_STOPWORDS = Collections.unmodifiableSet(stopwords);
		
		logger.info("Standard Stopwords size : " + STANDARD_STOPWORDS.size());
	}
	
}
