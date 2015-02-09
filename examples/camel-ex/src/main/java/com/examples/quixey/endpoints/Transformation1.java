package com.examples.quixey.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONException;
import org.json.JSONObject;

public class Transformation1 implements Processor {

	private static JSONObject transformation1(List<JSONObject> list)
			throws JSONException {
		JSONObject fjo = new JSONObject();
		List<JSONObject> ljo = new ArrayList<JSONObject>();
		for (int i = 0; i < list.size(); i = i + 2) {
			JSONObject jo = new JSONObject();
			jo.put("featureVector", list.get(i));
			jo.put("funcUrl", list.get(i + 1).get("appId"));
			ljo.add(jo);
		}
		fjo.put("funcUrls", ljo);
		fjo.put("baseModelName", "identity");
		return fjo;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		list.add("score");
		list.add("appId");
		JSONObject in = new JSONObject(exchange.getIn().getBody(String.class));
		JsonProcessor js = new JsonProcessor(in, list);
		exchange.getOut().setBody(
				Transformation1.transformation1(js.getValues()).toString());
	}
}
