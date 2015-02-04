package com.examples.camel.apibox;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.examples.camel.apibox.util.ToValidJson;

public class Proxy extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// Bridging / proxy

		from("jetty:http://0.0.0.0:8080/proxy/customer")
			.to("direct:customerProxy");

		from("direct:customerProxy")
			.to("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false")
			.process(new ToValidJson());

		
		
		
		
		from("direct:logger").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				System.out.println("logger - exchange: " +
						exchange.getIn().getBody(String.class));
			}
			
			
		});

		
	}
	
	

}
