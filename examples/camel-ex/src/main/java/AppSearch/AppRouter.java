package AppSearch;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.MetricsConstants;
import org.apache.camel.component.metrics.histogram.HistogramProducer;

public class AppRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from(
				"http://search-be.stage.quixey.com:8080/app_search?q=hello&bridgeEndpoint=true&throwExceptionOnFailure=false")
				.process(new Transformation()).to("direct:Transfor");

		from("direct:Transfor")
				.setHeader(MetricsConstants.HEADER_HISTOGRAM_VALUE,
						constant(992L))
				.to("metrics:histogram:simple.histogram?value=700")

				.to("http://scoring-i-96b9ba5c.us-west-1c.stage.quixey.com:8778/score")
				.setHeader(Exchange.HTTP_METHOD, constant("POST"))
				.process(new ScoringTransform());
	}
}
