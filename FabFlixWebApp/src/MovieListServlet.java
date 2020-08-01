import com.google.gson.JsonArray;
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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class MovieListServlet
 */
@WebServlet(name = "/MovieListServlet", urlPatterns = "/api/movieList")
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		
		try {
			Connection dbcon = dataSource.getConnection();
			
			String query = "select movies.id as movie_id, movies.title as title, movies.year as year, movies.director as director,(select group_concat(stars.id) from stars, stars_in_movies where movies.id = stars_in_movies.movieId and stars.id = stars_in_movies.starId) as starIdList ,(select group_concat(genres.name) from genres, genres_in_movies where genres_in_movies.movieId = movies.id and genres.id = genres_in_movies.genreId) as genreList, (select group_concat(genres.name) from genres, genres_in_movies where genres_in_movies.movieId = movies.id and genres.id = genres_in_movies.genreId) as genreIdList, (select group_concat(stars.name) from stars, stars_in_movies where movies.id = stars_in_movies.movieId and stars.id = stars_in_movies.starId) as starList, ratings.rating as rating from movies, ratings where ratings.movieId = movies.id and movies.title like ? and movies.year like ? and movies.director like ? and movies.id in (select stars_in_movies.movieId from stars_in_movies, stars where stars_in_movies.starId = stars.id and stars.name like ?) and movies.id in (select genres_in_movies.movieId from genres_in_movies, genres where genres.id = genres_in_movies.genreId and genres.name like ?)";
			
			query += "order by " + request.getSession().getAttribute("sortCriteria") + " " + request.getSession().getAttribute("sortOrder") + " limit ? offset ?";

			PreparedStatement statement = dbcon.prepareStatement(query);
			if(request.getSession().getAttribute("startingLetter") == null) {
				statement.setString(1, request.getSession().getAttribute("inputTitle").toString().trim() == ""?"%":("%" + request.getSession().getAttribute("inputTitle")).toString() + "%");
			}
			else {
				statement.setString(1, (request.getSession().getAttribute("startingLetter").toString() + "%"));
			}
			statement.setString(2, request.getSession().getAttribute("inputYear").toString().trim() == ""?"%":("%" + request.getSession().getAttribute("inputYear")).toString() + "%");
			statement.setString(3, request.getSession().getAttribute("inputDirector").toString().trim() == ""?"%":("%" + request.getSession().getAttribute("inputDirector")).toString() + "%");
			statement.setString(4, request.getSession().getAttribute("inputStar").toString().trim() == ""?"%":("%" + request.getSession().getAttribute("inputStar")).toString() + "%");
			statement.setString(5, request.getSession().getAttribute("genre").toString().trim() == ""?"%":("%" + (request.getSession().getAttribute("genre")).toString() + "%"));
			statement.setInt(6, Integer.parseInt(request.getSession().getAttribute("NumResultSelect").toString()));
			statement.setInt(7, Integer.parseInt(request.getSession().getAttribute("NumResultSelect").toString())*Integer.parseInt(request.getParameter("offset")));
			
			ResultSet rs = statement.executeQuery();
			
			JsonArray jsonArray = new JsonArray();
			
			while(rs.next()) {
				JsonObject jsonObject = new JsonObject();
				
				jsonObject.addProperty("movie_id", rs.getString("movie_id"));
				jsonObject.addProperty("title", rs.getString("title"));
				jsonObject.addProperty("year", rs.getInt("year"));
				jsonObject.addProperty("director", rs.getString("director"));
				jsonObject.addProperty("starIdList", rs.getString("starIdList"));
				jsonObject.addProperty("starList", rs.getString("starList"));
				jsonObject.addProperty("genreIdList", rs.getString("genreIdList"));
				jsonObject.addProperty("genreList", rs.getString("genreList"));
				jsonObject.addProperty("rating", rs.getFloat("rating"));
				
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
