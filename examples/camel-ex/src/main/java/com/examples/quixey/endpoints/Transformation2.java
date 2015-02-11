package com.examples.quixey.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Transformation2 {

	double confidence = 0.0;
	
	public List<Object> get_token_parser(int count,JSONObject query_parse_tree,
			double confidence1, List<JSONObject> jsonList) throws JSONException {
		List<JSONObject> tokens= new ArrayList<JSONObject>();;
		count = count+1;
		
		if (confidence1 == 0.0) {
			 
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
			List<Object> temp = new ArrayList<Object>();
			List<Object> temp1 = new ArrayList<Object>();
			temp.add(tokens);
			temp.add(confidence);
			temp1.add(temp);
			return temp1;
		}
		
		List<Object> token_parses = new ArrayList<Object>();
		for (int i = 0; i < query_parse_tree.getJSONArray("childrenNodes")
				.length(); i++) {
			List<JSONObject> temp_tokens = new ArrayList<JSONObject>();
			for(int j=0;j<tokens.size();j++){
				temp_tokens.add(tokens.get(j));
			}
			token_parses.addAll(get_token_parser(count,
					query_parse_tree.getJSONArray("childrenNodes")
							.getJSONObject(i), confidence, temp_tokens));
		}
		return token_parses;

	}
}
