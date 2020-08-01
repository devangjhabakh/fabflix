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
 * Servlet implementation class MovieServlet
 */
@WebServlet(name = "/MovieSearchServlet", urlPatterns = "/api/movieListSearch")
public class MovieSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    
	// Create a dataSource which is registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setAttribute("inputTitle", request.getParameter("inputTitle"));
		request.getSession().setAttribute("inputYear", request.getParameter("inputYear"));
		request.getSession().setAttribute("inputDirector", request.getParameter("inputDirector"));
		request.getSession().setAttribute("inputStar", request.getParameter("inputStar"));
		request.getSession().setAttribute("NumResultSelect", request.getParameter("NumResultSelect"));
		request.getSession().setAttribute("sortCriteria", "ratings.rating");
		request.getSession().setAttribute("genre", "");
		request.getSession().setAttribute("sortOrder", "desc");
		response.setStatus(200);
	}

}
