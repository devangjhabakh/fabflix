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
import java.sql.Date;
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
 * Servlet implementation class add_movie
 */
@WebServlet(name = "/AddMovieServlet", urlPatterns = "/add_movie/add")
public class AddMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
//	@Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		String title = request.getParameter("title");
		int year = Integer.parseInt(request.getParameter("year"));
		String director = request.getParameter("director");
		String star = request.getParameter("star");
		String genre = request.getParameter("genre");
		int starDOB = Integer.parseInt(request.getParameter("starDOB"));
		
		PrintWriter out = response.getWriter();
		
		try {
			
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
			
			Connection dbcon = dataSource.getConnection();
			
			String movieQuery = "select count(*) as movieCount from movies where movies.title = ? and movies.director = ? and movies.year = ?";
			
			PreparedStatement statement = dbcon.prepareStatement(movieQuery);
			statement.setString(1, title);
			statement.setString(2, director);
			statement.setInt(3, year);
			
			ResultSet rs = statement.executeQuery();
			
			rs.next();
			
			if(rs.getInt("movieCount") != 0) {
				
				response.setStatus(200);
				JsonObject finalMessage = new JsonObject();
				finalMessage.addProperty("message", "Movie Already Exists");
				
				out.write(finalMessage.toString());
				
			}
			
			else {
				
				//Getting the maximum star ID from the table
				
				String starQuery = "select max(stars.id) as starId from stars";
				
				Statement starStatement = dbcon.createStatement();
				ResultSet starRS = starStatement.executeQuery(starQuery);
				
				starRS.next();
				
				//Mutating the last starID to create a newer one
				
				String lastStarID = starRS.getString("starId");
				lastStarID = lastStarID.substring(2);
				int lastIDlen = lastStarID.length();
				int lastStarIDNumber = Integer.parseInt(lastStarID);
				lastStarIDNumber++;
				lastStarID = Integer.toString(lastStarIDNumber);
				while(lastStarID.length() < lastIDlen) {
					lastStarID = '0' + lastStarID;
				}
				lastStarID = "nm" + lastStarID;
				
				//Adding to star table
				
				String starInsertionQuery = "insert into stars values (?, ?, ?)";
				
				PreparedStatement starInsertionStatement = dbcon.prepareStatement(starInsertionQuery);
				starInsertionStatement.setString(1, lastStarID);
				starInsertionStatement.setString(2, star);
				starInsertionStatement.setInt(3, starDOB);
				
				starInsertionStatement.executeUpdate();
				starInsertionStatement.close();
				
				out.write("Added to stars!");
				
				//Checking if the genre exists
				
				String genreQuery = "select genres.id as genreID from genres where genres.name = ?";
				
				PreparedStatement genreStatement = dbcon.prepareStatement(genreQuery);
				genreStatement.setString(1, genre);
				ResultSet GenreRS = genreStatement.executeQuery();
				
				int genreID = 0;
				
				if(GenreRS.next()) {
					genreID = GenreRS.getInt("genreID");
					out.write("The genre already exists!");
				}
				
				//If the genre doesn't exist, get a new ID for it and insert it into the table
				
				else {
					String genreIDQuery = "select max(genres.id) as genreID from genres";
					
					Statement genreIDStatement = dbcon.createStatement();
					ResultSet GenreIdRS = genreIDStatement.executeQuery(genreIDQuery);
					
					GenreIdRS.next();
					
					genreID = GenreIdRS.getInt("genreID");
					genreID++;
					
					String genreInsertQuery = "insert into genres values (? , ?)";
					PreparedStatement genreInsertionStatement = dbcon.prepareStatement(genreInsertQuery);
					genreInsertionStatement.setInt(1, genreID);
					genreInsertionStatement.setString(2, genre);
					
					genreInsertionStatement.executeUpdate();
					
					genreInsertionStatement.close();
					GenreIdRS.close();
					
					out.write("Creating a new Genre!");
				}
				
				//Now, we have the starID, genreID, and are ready to insert the movie. We update movies, stars_in_movies, and genres_in_movies
				
				//First, getting ID for new movie.
				
				String MovieIDQuery = "select max(movies.id) as movieID from movies";
				
				Statement movieIDStatement = dbcon.createStatement();
				ResultSet MovieIdRS = movieIDStatement.executeQuery(MovieIDQuery);
				
				MovieIdRS.next();
				
				String movieIDString = MovieIdRS.getString("movieID");
				
				//Parsing the ID to create a new one to insert
				
				movieIDString = movieIDString.substring(2);
				lastIDlen = movieIDString.length();
				int movieID = Integer.parseInt(movieIDString);
				movieID++;
				movieIDString = Integer.toString(movieID);
				while(movieIDString.length() < lastIDlen) {
					movieIDString = '0' + movieIDString;
				};
				movieIDString = "tt" + movieIDString;
				
				//Adding the movie to movie DB
				
				String movieInsertionQuery = "insert into movies values (?, ?, ?, ?)";
				
				PreparedStatement movieInsertStatement = dbcon.prepareStatement(movieInsertionQuery);
				
				movieInsertStatement.setString(1, movieIDString);
				movieInsertStatement.setString(2, title);
				movieInsertStatement.setInt(3, year);
				movieInsertStatement.setString(4, director);
				
				movieInsertStatement.executeUpdate();
				
				movieInsertStatement.close();
				
				out.write("Added to Movie DB!");
				
				//Adding details to genres_in_movies
				
				String genres_in_moviesQuery = "insert into genres_in_movies values (?, ?)";
				
				PreparedStatement genres_in_moviesInsertStatement = dbcon.prepareStatement(genres_in_moviesQuery);
				
				genres_in_moviesInsertStatement.setInt(1, genreID);
				genres_in_moviesInsertStatement.setString(2, movieIDString);
				
				genres_in_moviesInsertStatement.executeUpdate();
				
				out.write("Added to genres_in_movies!");
				
				//Adding details to stars_in_movies
				
				String stars_in_moviesQuery = "insert into stars_in_movies values (?, ?)";
				
				PreparedStatement stars_in_moviesInsertStatement = dbcon.prepareStatement(stars_in_moviesQuery);
				
				stars_in_moviesInsertStatement.setString(1, lastStarID);
				stars_in_moviesInsertStatement.setString(2, movieIDString);
				
				stars_in_moviesInsertStatement.executeUpdate();
				
				out.write("Added to stars_in_movies!");
				
				//Closing all open connections.
				
				stars_in_moviesInsertStatement.close();
				genres_in_moviesInsertStatement.close();
				movieIDStatement.close();
				MovieIdRS.close();
				GenreRS.close();
				starRS.close();
				starStatement.close();
				genreStatement.close();
			}
			
			statement.close();
			rs.close();
			dbcon.close();
			response.setStatus(200);
			
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
