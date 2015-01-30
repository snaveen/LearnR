package spark;

public class RealSearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		spark.Spark.port(5000);
		spark.Spark.get("/search", new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				// TODO Auto-generated method stub
				return "[{search1:link1,search2:link2}]";
			}
		});
	}

}
