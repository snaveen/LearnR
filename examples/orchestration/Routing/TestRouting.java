package spark;

import java.util.ArrayList;
import java.util.List;

public class TestRouting {
static List<String> list = new ArrayList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		spark.Spark.port(8000);
		list.add("http://localhost:2000/adds");
		list.add("http://localhost:5000/search");
		spark.Spark.get("/add", new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				String body ="";
				for (int i= 0;i<list.size();i++)
				{
					ServiceRouting serrou = new ServiceRouting();
					serrou.transfer(list.get(i));
					//System.out.println(serrou.getA());
					body = body + serrou.getA();
				}
				// TODO Auto-generated method stub
				return body;
			}
		});

	}

}