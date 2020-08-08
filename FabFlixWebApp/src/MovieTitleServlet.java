

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Servlet implementation class MovieTitleServlet
 */
@WebServlet(name = "/MovieTitleServlet", urlPatterns = "/api/movietitles")
public class MovieTitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
//	@Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		String title = request.getParameter("term");
		
		PrintWriter out = response.getWriter();
		try {
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
			
			Connection dbcon = dataSource.getConnection();
			
			String query = "select movies.id as id, movies.title as title from movies where movies.title like ? or movies.title like ? limit 10";
			
			PreparedStatement statement = dbcon.prepareStatement(query);
			
			statement.setString(1, title + "%");
			statement.setString(2, "% " + title + "%");
			
			ResultSet rs = statement.executeQuery();
			
			JsonArray jsonArray = new JsonArray();
			
			while(rs.next()) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("label", rs.getString("title"));
				jsonObject.addProperty("value", rs.getString("id"));
				jsonArray.add(jsonObject);
			};
			
			out.write(jsonArray.toString());
			
			response.setStatus(200);
			
			rs.close();
			dbcon.close();
			statement.close();
		} catch(Exception e) {
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
