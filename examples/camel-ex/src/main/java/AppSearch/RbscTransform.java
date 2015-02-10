package AppSearch;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONArray;
import org.json.JSONObject;

public class RbscTransform implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		JSONObject in = new JSONObject(exchange.getIn().getBody(String.class));
		JSONArray array = new JSONArray();
		JSONObject out = new JSONObject();
		for (int i = 0; i < in.getJSONArray("funcUrls").length(); i++) {
			JSONObject ob = new JSONObject();

			ob.put("featureVector", in.getJSONArray("funcUrls")
					.getJSONObject(i).get("featureVector"));
			ob.put("funcUrl",
					in.getJSONArray("funcUrls").getJSONObject(i).get("funcUrl"));
			array.put(ob);
		}
		out.put("funcUrls", array);
		exchange.getOut().setBody(out.toString());
		System.out.println(out);
	}

}
