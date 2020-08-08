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
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.sql.Statement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SingleMovieServlet
 */
@WebServlet(name = "/SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
//	@Resource(name = "jdbc/moviedb")
//	private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		
		String id = request.getParameter("id");
		
		PrintWriter out = response.getWriter();
		
		try {
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
			
			//Get a connection from DataSource
			Connection dbcon = dataSource.getConnection();
			
			String query = "select movies.id as id, movies.title as title, movies.year as year, movies.director as director, (select group_concat(genres.name) from genres, genres_in_movies where movies.id = genres_in_movies.movieId and genres.id = genres_in_movies.genreId) as genreList,(select group_concat(stars.name) from stars, stars_in_movies where stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id) as starList, (select group_concat(stars.id) from stars, stars_in_movies where stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id) as starIdList, ratings.rating from movies, ratings where movies.id = ?";
			
			//Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);
			
			//Replace the first ? with our id
			statement.setString(1, id);
			
			//Perform the query
			ResultSet rs = statement.executeQuery();
			
			JsonObject jsonObject = new JsonObject();
			rs.next();
			
			jsonObject.addProperty("id", rs.getString("id"));
			jsonObject.addProperty("title", rs.getString("title"));
			jsonObject.addProperty("year", rs.getInt("year"));
			jsonObject.addProperty("director", rs.getString("director"));
			jsonObject.addProperty("genreList", rs.getString("genreList"));
			jsonObject.addProperty("starList", rs.getString("starList"));
			jsonObject.addProperty("starIdList", rs.getString("starIdList"));
			
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
