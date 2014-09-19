package com.learnr.core.xml;

public class TagSearch {
	/**
	 * Method to give substring of a string
	 * 
	 * @param doc
	 *            (Patent document as string)
	 * @param start_tag
	 *            (starting point of a sub string)
	 * @param end_tag
	 *            (ending point of a sub string)
	 * @return(sub string)
	 */
	public static String getContent(String doc, String start_tag, String end_tag) {
		if (doc.indexOf(start_tag) < doc.indexOf(end_tag)) {
			String content = doc.substring(doc.indexOf(start_tag),
					doc.indexOf(end_tag));
			return content;
		} else
			return "No" + start_tag;

	}

}
