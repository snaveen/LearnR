package spark;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RoutingPractise {
	// this path isfixed beacuseit checking in thepresent project for the folder
	private final String inputPath = "Resource/InputFolder";

	// path to the folder to copy the iles

	public void transferTheFile() throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			public void configure() {
				// from method tells from where the files has to be copied
				// process method is used to change the body and header .. of
				// the files before copying to the respective folder
				// to method tels the path where the files has to be copied.
				from("file:" + inputPath + "?noop=true").process(
						new Processor() {
							public void process(Exchange exchange)
									throws Exception {
								// gives the body of the file
								String body = exchange.getIn().getBody(
										String.class);
								// changes the body
								exchange.getOut().setBody("Hello " + body);
								exchange.getOut().setHeaders(
										exchange.getIn().getHeaders());
							}
						}).to("file:" + outputPath);
			}
		});
	}

}
