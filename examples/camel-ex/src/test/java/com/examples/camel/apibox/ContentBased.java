package com.examples.camel.apibox;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.examples.camel.apibox.util.ToValidJson;

public class ContentBased extends RouteBuilder {
	public static Object value;
	
	@Override
	public void configure() throws Exception {

		from("jetty:http://0.0.0.0:8080/content").process(new Processor(){

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("THis is what we want: "+exchange.getIn().getHeader("service"));
				System.out.println("This are headers :"+exchange.getIn().getHeaders());
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");
				value = exchange.getIn().getHeader("service");
			}
			
		})
		.choice()
			.when(header("service").isEqualTo("customer"))
				.to("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false").process(new ToValidJson())
			.otherwise()
				.to("http://localhost:5000/order?bridgeEndpoint=true&throwExceptionOnFailure=false").process(new ToValidJson());
		
		
		from("jetty:http://0.0.0.0:8080/geo_search")
			.to("http://localhost:5000/geolocation")
			.to("http://localhost:5000/search");
		
	}

}
