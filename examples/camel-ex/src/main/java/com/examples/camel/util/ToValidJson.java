package com.examples.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ToValidJson implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String inBody = exchange.getIn().getBody(String.class);
		String outBody = inBody.replace("'", "\"");
		
		// NOTE you are loosing headers and stuff
		exchange.getOut().setBody(outBody);
	}
}
