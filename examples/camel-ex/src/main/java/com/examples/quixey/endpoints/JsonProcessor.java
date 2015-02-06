package com.examples.quixey.endpoints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonProcessor {
	JSONObject json = new JSONObject();
	List<String> list;
	List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
	int count = 0;

	public JsonProcessor(JSONObject obj, List<String> list) {
		this.list = list;
		this.json = obj;
	}

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub

	}

	public List<JSONObject> getValues() throws JSONException {
		
		Iterator keys = json.keys();
		//System.out.println("++++++++++++++++:  "+keys);
		while (keys.hasNext()) {
			
			String key = (String) keys.next();
			//System.out.println("key:   "+key);
			if (list.contains(key)) {
				JSONObject out = new JSONObject();
				//System.out.println("in if og getValues");
				out.put(key, json.get(key));
				jsonObjList.add(out);
			} else if (json.get(key) instanceof JSONObject) {
				//System.out.println("in JSONOBject instance ");
				value(json.getJSONObject(key));
			} else if (json.get(key) instanceof JSONArray) {
				//System.out.println("********************in JSONOArray instance ");
				getJSONObjectFromJSONArray(json.getJSONArray(key));
				
			}

		}
		return jsonObjList;
	}

	private void value(JSONObject json) throws JSONException {
		Iterator keys = json.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			//System.out.println("1key  :"+key);
			if (list.contains(key)) {
				JSONObject out = new JSONObject();
				//System.out.println("in if of value");
				out.put(key, json.get(key));
				//System.out.println("-------------  "+out.get(key));
				jsonObjList.add(out);
				
			} else if (json.get(key) instanceof JSONObject) {
				value(json.getJSONObject(key));
			} else if (json.get(key) instanceof JSONArray) {
				getJSONObjectFromJSONArray(json.getJSONArray(key));
			}
		}
	}

	private void getJSONObjectFromJSONArray(JSONArray json)
			throws JSONException {
		for (int i = 0; i < json.length(); i++) {
			//System.out.println("IIIIIIIIIIIIIIIIIIII  "+i);
			if (json.get(i) instanceof JSONObject) {
				//System.out.println("!!!! in JSONObject of getJSONObjectFromJSONArray");
				value(json.getJSONObject(i));
			} else if (json.get(i) instanceof JSONArray) {
				getJSONObjectFromJSONArray(json.getJSONArray(i));
			}

		}
	}

}
