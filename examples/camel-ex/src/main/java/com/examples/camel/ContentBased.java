package com.examples.camel;

import org.apache.camel.builder.RouteBuilder;

import com.examples.camel.util.ToValidJson;

public class ContentBased extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {

		from("jetty:http://0.0.0.0:8080/content")
		.choice()
			.when(header("service").isEqualTo("customer"))
				.to("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false").process(new ToValidJson())
			.otherwise()
				.to("http://localhost:5000/order?bridgeEndpoint=true&throwExceptionOnFailure=false").process(new ToValidJson());
		
	}

}
