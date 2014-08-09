package com.learnr.core.text;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.util.Verify;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatizer {
	
	private static final Logger logger = LoggerFactory.getLogger(Lemmatizer.class);
	
	private static final StanfordCoreNLP pipeline;
	
	static {
		Properties props;
	    props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");

	    // StanfordCoreNLP loads a lot of models, so you probably
	    // only want to do this once per execution
	    pipeline = new StanfordCoreNLP(props);
	}
	
	public static List<String> lemmatize(String inStr) {
		Verify.hasLength(inStr);
		logger.debug("Incoming String for lemmatization : " + inStr);
		
		List<String> lemmas = new LinkedList<String>();

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(inStr);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // Iterate over all of the sentences found
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences) {
	        // Iterate over all tokens in a sentence
	        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        	
	            // Retrieve and add the lemma for each word into the list of lemmas
	            lemmas.add(token.get(LemmaAnnotation.class));
	        }
	    }

	    return lemmas;
	}

}
