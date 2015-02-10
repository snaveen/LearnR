package AppSearch;

import org.apache.camel.builder.RouteBuilder;

public class QupRouter extends RouteBuilder {
	private static String QupService = "http://127.0.0.1:5000/Qup";

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from(QupService).process(new QupTransformation());

	}
}
