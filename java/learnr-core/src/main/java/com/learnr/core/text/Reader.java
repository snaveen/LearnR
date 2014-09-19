package com.learnr.core.text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
	  public static List<String> getCorpusWords(String inputfile)
	            throws FileNotFoundException {
	        // String listOfwords=inputfile.toString().split("\t")[0];
	        @SuppressWarnings("resource")
	        BufferedReader reader = new BufferedReader(new FileReader(inputfile));
	        String line = null;
	        List<String> listOfWords = new ArrayList<String>();
	        try {
	            while ((line = reader.readLine()) != null) {
	                String word = line.split("\t")[0];
	                listOfWords.add(word);
	            }
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        return listOfWords;

	    }
}
