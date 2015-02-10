package com.examples.quixey.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Transformation2 {
	List<JSONObject> tokens = new ArrayList<JSONObject>();
	List<Object> token_parses = new ArrayList<Object>();
	double confidence = 0.0;

	public List<Object> get_token_parser(JSONObject query_parse_tree,
			double confidence1, List<JSONObject> jsonList) throws JSONException {
		if (confidence1 == 0) {
			confidence = 1.0;
		} else {

			tokens = jsonList;
			confidence = confidence1;
		}
		confidence = confidence
				* Double.parseDouble(query_parse_tree
						.getString("edgeConfidence"));
		if (query_parse_tree.has("nodeValue")) {

			tokens.add(query_parse_tree.getJSONObject("nodeValue"));
		}
		if (query_parse_tree.getJSONArray("childrenNodes").isNull(0)) {
			List<Object> listobj = new ArrayList<Object>();
			listobj.add(tokens);
			listobj.add(confidence);
			return listobj;
		}
		for (int i = 0; i < query_parse_tree.getJSONArray("childrenNodes")
				.length(); i++) {

			token_parses.add(get_token_parser(
					query_parse_tree.getJSONArray("childrenNodes")
							.getJSONObject(i), confidence, jsonList));
		}
		return token_parses;

	}
}
