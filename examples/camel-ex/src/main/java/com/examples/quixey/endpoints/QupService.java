package com.examples.quixey.endpoints;


import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.json.JSONObject;

public class QupService {
	static List<Object> jsa  = new ArrayList<Object>();
	static List<JSONObject> aa = new ArrayList<JSONObject>();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CamelContext cc = new DefaultCamelContext();
		cc.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from("jetty:http://0.0.0.0:8080/aggr").
				to("http://127.0.0.1:2000/qupservice?bridgeEndpoint=true&throwExceptionOnFailure=false").process(
						new Processor() {

							@Override
							public void process(Exchange exchange)
									throws Exception {
								// TODO Auto-generated method stub
								String body = exchange.getIn().getBody(
										String.class);
								JSONObject in = new JSONObject(body);
								Transformation2 t2 = new Transformation2();
								
								jsa = t2.get_token_parser(0,
										in.getJSONObject("queryParseTree"),
										0.0, aa);
								exchange.getOut().setBody(jsa);
								

							}
						});
			}
		});
		
		cc.start();
		Thread.sleep(1130);
	}

}
