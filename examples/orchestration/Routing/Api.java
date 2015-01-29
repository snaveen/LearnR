package spark;
import static spark.Spark.*;
public class Api {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		get("/hello", (req, res) -> "Hello World");
		get("/hello/:name", (request, response) -> {
		    return "Hello: " + request.params(":name");
		});
	}

}
