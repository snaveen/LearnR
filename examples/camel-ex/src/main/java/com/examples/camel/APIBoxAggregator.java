package com.examples.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

public class APIBoxAggregator extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		
		from("jetty:http://0.0.0.0:8080/aggr")
			.enrich("direct:service_aggregate").setBody(property(Exchange.GROUPED_EXCHANGE));
	
		from("direct:service_aggregate")
		  .multicast(new GroupedExchangeAggregationStrategy()).parallelProcessing()
		    .enrich("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false")
		    .enrich("http://localhost:5000/order?bridgeEndpoint=true&throwExceptionOnFailure=false")
		  .end();
		
	}
	
}
