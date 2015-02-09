package com.examples.quixey.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.json.JSONObject;

public class AppSearch {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CamelContext cc = new DefaultCamelContext();
		cc.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from(
						"http://search-be.stage.quixey.com:8080/app_search?q=hello")
						.process(new Processor() {

							@Override
							public void process(Exchange exchange)
									throws Exception {
								// TODO Auto-generated method stub
								List<String> list = new ArrayList<String>();
								list.add("score");
								list.add("appId");
								JSONObject in = new JSONObject(exchange.getIn()
										.getBody(String.class));
								JsonProcessor js = new JsonProcessor(in, list);
								System.out.println("@@@@@@@@@@@@@@@@@@@@ :"
										+ js.getValues());
							}
						});
			}
		});
		cc.start();
		Thread.sleep(2000);
		// cc.stop();
	}

}
