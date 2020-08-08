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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DashboardLoginServlet
 */
@WebServlet(name = "/DashboardLoginServlet", urlPatterns = "/api/dashboard_login")
public class DashboardLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
//	@Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Starting check!");
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		System.out.println("Got Values!");
		
		try {
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
			
			Connection dbcon = dataSource.getConnection();
			
			String query = "select count(*) as customerCount from employee where employee.email = ? and employee.password = ?";
			
			PreparedStatement statement = dbcon.prepareStatement(query);
			
			statement.setString(1, email);
			statement.setString(2, password);
			
			System.out.println("The query is "+ statement.toString());
			
			ResultSet rs = statement.executeQuery();
			
			rs.next();
			
			if(rs.getInt("customerCount") != 0) {
				response.setStatus(200);
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("message", "Correct!");
				out.write(jsonObject.toString());
			}
			else {
				response.setStatus(500);
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("message", "Incorrect!");
				jsonObject.addProperty("query", statement.toString());
				System.out.println("Oops!");
				out.write(jsonObject.toString());
			}
			rs.close();
			statement.close();
			dbcon.close();
		} catch(Exception e) {
			response.setStatus(500);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", e.getMessage());
			out.write(jsonObject.toString());
		}
		out.close();
	}

}
