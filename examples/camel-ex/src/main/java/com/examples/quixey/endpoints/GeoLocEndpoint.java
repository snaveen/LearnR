package com.examples.quixey.endpoints;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoLocEndpoint extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("jetty:http://localhost:5000/sdk/android/v1/geolocate")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						System.out.println("@@@@@ :"
								+ exchange.getIn().getHeaders());
						exchange.getIn().setHeader(
								Exchange.HTTP_QUERY,
								"ip_address="
										+ exchange.getIn().getHeader(
												"client_ip"));

					}
				})
				.to("http://geo-service-i-5a91fe99.us-west-1c.stage.quixey.com:10106/geodata?bridgeEndpoint=true&throwExceptionOnFailure=false")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						String inBody = exchange.getIn().getBody(String.class);
						System.out.println(inBody);
						String outBody = transformation(inBody);
						exchange.getOut().setBody(outBody);
					}

					private String transformation(String inBody)
							throws JSONException {
						// TODO Auto-generated method stub
						JSONObject ob = new JSONObject(inBody);
						System.out.println(ob.get("edge") + "****************");
						String country = new JSONObject(ob.getString("edge"))
								.getString("country");
						String cou = "Country :".concat(country);
						String city = new JSONObject(ob.getString("edge"))
								.getString("city");
						String c = "City :".concat(city);
						String result = "{" + cou + "," + c + "}";
						return result;
					}
				});

	}
}
