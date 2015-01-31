package com.examples.camel;


import org.apache.camel.Exchange;

public class ServiceRouting implements org.apache.camel.Processor {
	private static String a;

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		String content = exchange.getIn().getBody(String.class);
		ServiceRouting.setA(content);
		
	}
	public String getA() {
		return a;
	}

	public static void setA(String a) {
		ServiceRouting.a = a;
	}


	
}
