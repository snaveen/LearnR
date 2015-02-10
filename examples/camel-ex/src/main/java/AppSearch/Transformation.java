package AppSearch;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONArray;
import org.json.JSONObject;

public class Transformation implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		JSONObject in = new JSONObject(exchange.getIn().getBody(String.class));
		JSONArray jList = new JSONArray();
		JSONObject out = new JSONObject();
		for (int i = 0; i < in.getJSONArray("results").length(); i++) {
			JSONObject id = new JSONObject();
			id.put("funcUrl",
					in.getJSONArray("results").getJSONObject(i).get("id"));
			id.put("featureVector",
					new JSONObject().put("score", in.getJSONArray("results")
							.getJSONObject(i).get("score")));
			jList.put(id);

		}
		out.put("funcUrls", jList);
		out.put("baseModelName", "identity");
		System.out.println(out);
		exchange.getOut().setBody(out.toString());
	}
}
