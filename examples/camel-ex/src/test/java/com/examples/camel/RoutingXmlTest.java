package com.examples.camel;

import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RoutesDefinition;


public class RoutingXmlTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new RoutingXmlTest().run();
		//InputStream is = getClass().getResourceAsStream("routing.xml");
		//RoutesDefinition routes = context.loadRoutesDefinition(is);
		//context.addRouteDefinitions(routes.getRoutes());
		
	}
	@SuppressWarnings("deprecation")
	public void run() throws Exception{
		CamelContext context = new DefaultCamelContext();
		InputStream is = getClass().getResourceAsStream("routing.xml");
		RoutesDefinition routes = context.loadRoutesDefinition(is);
		context.addRouteDefinitions(routes.getRoutes());
		context.start();
		Thread.sleep(5000);
		context.stop();
	}

}
