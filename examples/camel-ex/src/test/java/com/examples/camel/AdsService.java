package com.examples.camel;

import spark.Request;
import spark.Response;
import spark.Route;

public class AdsService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		spark.Spark.port(2000);
		spark.Spark.get("/adds", new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				// TODO Auto-generated method stub
				return "[{ad1:Trimmer,ad2:Shaving cream}]";
			}
		});

	}
}
