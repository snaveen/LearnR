package Routing;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileCopier {
	private static String inputpath;
	private static String outputpath;

	public FileCopier(String input, String output) {
		inputpath = input;
		outputpath = output;
	}

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(transfer(inputpath, outputpath));

		context.start();

		Thread.sleep(10000);
		context.stop();
	}

	private static RouteBuilder transfer(String inputpath, String outputpath) {
		RouteBuilder builder = new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from("file:" + inputpath + "?noop=true").to(
						"file:" + outputpath);
			}
		};
		return builder;

	}
}
