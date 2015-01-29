package spark;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RoutingForUrls {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				from("http://localhost:4567/user").process(
						new Processor() {
							public void process(Exchange exchange)
									throws Exception {
								System.out.println("In process");
								String contentType = exchange.getIn()
										.getHeader(Exchange.CONTENT_TYPE,
												String.class);
								String path = exchange.getIn().getHeader(
										Exchange.HTTP_URI, String.class);
								System.out.println("contentType: "
										+ contentType);
								System.out.println("path: " + path);
							}
						}).to("http://localhost:4567/name");

			}
		});
		context.start();
		Thread.sleep(1000);
		context.stop();

	}

}
