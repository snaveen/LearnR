package com.examples.camel.apibox;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Reference: http://stackoverflow.com/questions/10610820/apache-camel-to-aggregate-multiple-rest-service-responses
 */
public class APIBoxAggregator extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("jetty:http://0.0.0.0:8080/aggr").enrich("direct:service_aggregate").setBody(
				property(Exchange.GROUPED_EXCHANGE));

		from("direct:service_aggregate").multicast(new SimpleAggrStrategy()).parallelProcessing()
				.enrich("http://localhost:5000/customer?bridgeEndpoint=true&throwExceptionOnFailure=false")
				.enrich("http://localhost:5000/order?bridgeEndpoint=true&throwExceptionOnFailure=false").end();

	}

	/* --- Utilities --- */

	class SimpleAggrStrategy extends GroupedExchangeAggregationStrategy {

		@Override
		public void onCompletion(Exchange exchange) {

			List<Exchange> list = (List<Exchange>) exchange.getProperty(Exchange.GROUPED_EXCHANGE);
			try {
				JSONObject out = new JSONObject();
				for (int i = 0; i < list.size(); i++) {
					String inStr = list.get(i).getIn().getBody(String.class);
					Object outStr = inStr.startsWith("[") ? new JSONArray(inStr) : new JSONObject(inStr);
					out.put("result" + i, outStr);
				}
				exchange.setProperty(Exchange.GROUPED_EXCHANGE, out);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
