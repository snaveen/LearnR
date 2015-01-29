package spark;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RoutingPractise {
	private final String inputPath = "Resource/InputFolder";
	private final String outputPath = "Resource/OutPutFolder";

	public void transferTheFile() throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			public void configure() {

				from("file:" + inputPath + "?noop=true").process(
						new Processor() {
							public void process(Exchange exchange)
									throws Exception {
								String body = exchange.getIn().getBody(
										String.class);

								exchange.getOut().setBody("Hello " + body);
								exchange.getOut().setHeaders(
										exchange.getIn().getHeaders());
							}
						}).to("file:" + outputPath);
			}
		});
	}

}
