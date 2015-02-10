package AppSearch;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class InsideSearchRoute extends RouteBuilder {
	private static String insideService = "";
	private static String scoringService = "http://scoring-i-96b9ba5c.us-west-1c.stage.quixey.com:8778/score";

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from(insideService).process(new Transformation()).to(scoringService)
				.setHeader(Exchange.HTTP_METHOD, constant("POST"))
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						System.out.println(exchange.getIn().getBody(
								String.class));

					}
				});
	}

}
