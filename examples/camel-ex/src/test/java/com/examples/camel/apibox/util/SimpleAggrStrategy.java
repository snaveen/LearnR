package com.examples.camel.apibox.util;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

//simply combines Exchange String body values using '+' as a delimiter
public class SimpleAggrStrategy extends GroupedExchangeAggregationStrategy {

	@Override
    public void onCompletion(Exchange exchange) {
		System.out.println("onComplete called");
		
        // lets be backwards compatible
        // TODO: Remove this method in Camel 3.0
        List<Exchange> list = (List<Exchange>) exchange.getProperty(Exchange.GROUPED_EXCHANGE);
        if (list != null) {
        	String outBody = "{";
    		for (int i = 0; i < list.size(); i++) {
    			String eBody = list.get(i).getIn().getBody(String.class);
    			
    			outBody += "\"result" + i + "\":" + eBody.replace("'", "\"");
    			if(i + 1 != list.size()) {
    				outBody += ",";
    			}
    		}
    		
    		outBody += "}";
    		System.out.println(outBody);
    		
    		exchange.setProperty(Exchange.GROUPED_EXCHANGE, outBody);
    		
        } else {
        	System.out.println("grouped exchange property is null");
        }
		
	}
	
}
