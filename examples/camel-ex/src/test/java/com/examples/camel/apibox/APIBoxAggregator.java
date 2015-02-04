package com.examples.camel.apibox;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.examples.camel.apibox.util.SimpleAggrStrategy;

public class APIBoxAggregator extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// http://stackoverflow.com/questions/10610820/apache-camel-to-aggregate-multiple-rest-service-responses
		
		
		from("jetty:http://0.0.0.0:8080/aggr")
			.enrich("direct:service_aggregate").setBody(property(Exchange.GROUPED_EXCHANGE));
	
		from("direct:service_aggregate")
		  .multicast(new SimpleAggrStrategy()).parallelProcessing()
		    .enrich("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false")
		    .enrich("http://localhost:5000/order?bridgeEndpoint=true&throwExceptionOnFailure=false")
		  .end();
		
		
	}
	
}
