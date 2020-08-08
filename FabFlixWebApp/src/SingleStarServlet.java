import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class SingleStarServlet
 */
@WebServlet(name = "/SingleStarServlet", urlPatterns = "/api/single-star")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Create a dataSource which is registered in web.xml
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		//Retrieve parameter id from URL request
		String id = request.getParameter("id");
		
		//Output stream to STDOUT
		PrintWriter out = response.getWriter();
		
		try {
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
			
			//Get a connection from dataSource
			Connection dbcon = dataSource.getConnection();
			String query = "select stars.name as name, stars.birthYear as YOB, (select group_concat(movies.title) from movies, stars_in_movies where stars.id = stars_in_movies.starId and stars_in_movies.movieId = movies.id) as movieTitleList, (select group_concat(movies.id) from movies, stars_in_movies where stars.id = stars_in_movies.starId and stars_in_movies.movieId = movies.id) as movieIdList from stars where stars.id = ?";
			
			//Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);
			
			//Set the parameter represented by ? in the query to the ID we get from the URL.
			//The number 1 represents the first occurrence of the ?
			statement.setString(1, id);
			
			//Perform the query
			ResultSet rs = statement.executeQuery();
			rs.next();
			
			JsonObject jsonObject = new JsonObject();
			
			jsonObject.addProperty("name", rs.getString("name"));
			jsonObject.addProperty("YOB", rs.getString("YOB"));
			jsonObject.addProperty("movieTitleList", rs.getString("movieTitleList"));
			jsonObject.addProperty("movieIdList", rs.getString("movieIdList"));
			
			//Write JSON string to Output
			out.write(jsonObject.toString());
			//Set response status to 200 (OK)
			response.setStatus(200);
			
			rs.close();
			statement.close();
			dbcon.close();
		} catch (Exception e) {
			//Write error message to the JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			
			//Set response status to 500 (Internal server error)
			response.setStatus(500);
		}
		out.close();
	}
}
