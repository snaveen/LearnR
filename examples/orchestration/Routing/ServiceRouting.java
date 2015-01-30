package spark;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ServiceRouting {
	private static String a;

	public void transfer(String uri) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from(uri).process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						String content = exchange.getIn().getBody(String.class);
						ServiceRouting.setA(content);
					}

				});
			}
		});
		context.start();
		Thread.sleep(5000);
		context.stop();

	}

	public String getA() {
		return a;
	}

	public static void setA(String a) {
		ServiceRouting.a = a;
	}

}
