package AppSearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;

public class ScoringTransform implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		JSONObject in = new JSONObject(exchange.getIn().getBody(String.class));
		JSONObject scoreByFncUrls=new JSONObject();
		List<Object> list;
		JSONObject results = in.getJSONObject("results");
		for (int i = 0; i < results.length(); i++) {
			if (results.getJSONObject(results.names().get(i).toString())
					.getBoolean("succeeded")) {
				list = new ArrayList<Object>();
				list.add(results.names().get(i).toString());
				list.add(results.getJSONObject(
						results.names().get(i).toString()).getDouble("score"));
				System.out.println(list);
				System.out.println(")))))))))))))");
			}

		}
	}

}
