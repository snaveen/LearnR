package AppSearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QupTransformation implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		JSONObject in = new JSONObject(exchange.getIn().getBody(String.class));
		List<JSONObject> token = new ArrayList<JSONObject>();
		float confidence = 0;
		getTokenParser(in.getJSONObject("queryParseTree"), token, confidence);
	}

	private JSONObject transformParse(List<JSONObject> tokenParser,
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

	private List<Object> getTokenParser(JSONObject jsonObject,
			List<JSONObject> token, float confidence)
			throws NumberFormatException, JSONException {
		// TODO Auto-generated method stub

		if (confidence == 0) {
			confidence = 1;
		}
		confidence = confidence
				* Float.parseFloat(jsonObject.getString("edgeConfidence"));
		if (jsonObject.has("nodeValue")) {
			token.add(jsonObject.getJSONObject("nodeValue"));
		}

		if (jsonObject.getJSONArray("childrenNodes").isNull(0)) {
			// System.out.println("==========================");
			List<Object> list = new ArrayList<Object>();
			list.add(confidence);
			list.add(token);
			return list;
		}
		List<Object> tokenParses = new ArrayList<Object>();
		for (int i = 0; i < jsonObject.getJSONArray("childrenNodes").length(); i++) {
			tokenParses.add(getTokenParser(
					jsonObject.getJSONArray("childrenNodes").getJSONObject(i),
					token, confidence));

		}
		System.out.println(tokenParses + "+++++++++++++++");
		return tokenParses;

	}
}
