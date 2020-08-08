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
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "/LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
//	@Resource(name = "jdbc/moviedb")
//	private DataSource dataSource;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		try {			
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
            
			Connection dbcon = dataSource.getConnection();
            
			String query = "select count(*) as numUsers, customers.id as id from customers where email = ? group by customers.id";
			PreparedStatement statement = dbcon.prepareStatement(query);
			
			statement.setString(1, email);
	 	
			ResultSet rs = statement.executeQuery();

			new VerifyPassword();
			if(rs.next() && VerifyPassword.verifyCredentials(email, password)) {
				request.getSession().setAttribute("user", new User(email, rs.getString("id")));
				response.setStatus(200);
			}
			else {
				response.setStatus(500);
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("message", "Either the email or the password is incorrect!");
				jsonObject.addProperty("query", statement.toString());
				jsonObject.addProperty("user", email);
				jsonObject.addProperty("pw", password);
				out.write(jsonObject.toString());
			}
			
			rs.close();
			statement.close();
			dbcon.close();
		} catch (Exception e) {
			response.setStatus(500);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", e.getMessage());
			out.write(jsonObject.toString());
		}
	out.close();
	}

}
