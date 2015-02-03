package com.examples.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

public class Proxy extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// File moving
//		from("file:/home/milli/workspace/python/apibox/quixey")
//			.to("file:/home/milli/git/LearnR/examples/camel-ex/data");
		
		// Bridging / proxy

		from("jetty:http://0.0.0.0:8080/proxy/customer")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println(exchange.getExchangeId());
	
					System.out.println(exchange.getIn().getHeaders());
					System.out.println(exchange.getIn().getBody());
				}
			})
			.to("direct:customerProxy");
//			.to("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false");

		from("direct:customerProxy")
			.to("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false");


		
	}
	
	

}
