

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class FirstLetterSelector
 */
@WebServlet(name = "/FirstLetterSelector", urlPatterns = "/api/letterList")
public class FirstLetterSelector extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		
		try {
			Connection dbcon = dataSource.getConnection();
			
			String query = "select distinct substring(movies.title,1,1) as startingLetter from movies";

			Statement statement = dbcon.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			
			JsonArray jsonArray = new JsonArray();
			
			while(rs.next()) {
				JsonObject jsonObject = new JsonObject();
				
				jsonObject.addProperty("startingLetter", rs.getString("startingLetter"));
				
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
