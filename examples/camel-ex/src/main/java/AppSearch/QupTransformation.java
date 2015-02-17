package AppSearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QupTransformation implements Processor {
	private float confidence = 0;
	private List<JSONObject> token = new ArrayList<JSONObject>();

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		JSONObject in = new JSONObject(exchange.getIn().getBody(String.class));

		getTokenParser(in.getJSONObject("queryParseTree"), confidence, token);
		System.out.println(token);

	}

	private JSONObject transformParse(List<Object> tokenParser,
			List<JSONObject> token, float confidence) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject out = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < 10; i++) {
			JSONObject ob = new JSONObject();
			ob.put("lexeme", token.get(i).getString("queryToken"));
			ob.put("start", token.get(i).getString("queryTokenStartIndex"));
			ob.put("end", token.get(i).getString("queryTokenEndIndex"));
			ob.put("genericEntity",
					new JSONObject().put("@id",
							token.get(i).getJSONObject("parsedTokenData")
									.getJSONObject("id").getString("id")));
			ob.put("genericEntity",
					new JSONObject().put("@type",
							token.get(i).getJSONObject("parsedTokenData")
									.getJSONObject("id").getString("typeName")));

			ob.put("tokens", array);
			ob.put("confidence", confidence);
			array.put(ob);
		}
		System.out.println(out);
		System.out.println("*******************");
		return out;
	}

	private List<Object> getTokenParser(JSONObject query_parse_tree,
			float confidence1, List<JSONObject> jsonList) throws JSONException {
		List<JSONObject> tokens = new ArrayList<JSONObject>();
		

		if (confidence1 == 0) {
			confidence = 1;
		} else {
			tokens = jsonList;
			confidence = confidence1;
		}
		confidence = confidence
				* Float.parseFloat(query_parse_tree.getString("edgeConfidence"));
		if (query_parse_tree.has("nodeValue")) {
			tokens.add(query_parse_tree.getJSONObject("nodeValue"));
		}

		if (query_parse_tree.getJSONArray("childrenNodes").isNull(0)) {
			List<Object> temp = new ArrayList<Object>();
			List<Object> list = new ArrayList<Object>();
			temp.add(tokens);
			temp.add(confidence);
			list.add(temp);
			return list;
		}

		List<Object> token_parses = new ArrayList<Object>();
		for (int i = 0; i < query_parse_tree.getJSONArray("childrenNodes")
				.length(); i++) {
			List<JSONObject> temp_tokens = new ArrayList<JSONObject>();
			for (int j = 0; j < tokens.size(); j++) {
				temp_tokens.add(tokens.get(j));
			}
			token_parses.addAll(getTokenParser(
					query_parse_tree.getJSONArray("childrenNodes")
							.getJSONObject(i), confidence, temp_tokens));
		}
		return token_parses;

	}
}
