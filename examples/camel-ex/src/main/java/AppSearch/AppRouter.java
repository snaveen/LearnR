package AppSearch;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class AppRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from(
				"http://search-be.stage.quixey.com:8080/app_search?q=hello&bridgeEndpoint=true&throwExceptionOnFailure=false")
				.process(new Transformation()).to("direct:TransService");

		from("direct:TransService")
				.to("http://scoring-i-96b9ba5c.us-west-1c.stage.quixey.com:8778/score")
				.setHeader(Exchange.HTTP_METHOD, constant("POST"))
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						System.out.println(exchange.getIn().getBody(
								String.class));
						System.out.println(exchange.getIn().getHeaders());

					}
				});
	}
}
