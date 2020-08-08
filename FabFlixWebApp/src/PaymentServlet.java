import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.time.LocalDate;
import java.util.HashMap;

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
import java.time.LocalDate;
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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PaymentServlet
 */
@WebServlet(name = "/PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
//	@Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		
		try {
			Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
			
			Connection dbcon = dataSource.getConnection();
			
			String query = "select count(*) as count from creditcards where creditcards.firstname = ? and creditcards.lastname = ? and creditcards.expiration = ? and creditcards.id = ?"                              ;
			
			PreparedStatement statement = dbcon.prepareStatement(query);
			
			statement.setString(1, request.getParameter("inputFName"));
			statement.setString(2, request.getParameter("inputLName"));
			statement.setString(3, request.getParameter("inputDate"));
			statement.setString(4, request.getParameter("inputCardNumber"));
			
			ResultSet rs = statement.executeQuery();
			
			rs.next();
			
			if(rs.getInt("count") == 0)
				response.setStatus(500);
			
			else {
				User current = (User) request.getSession().getAttribute("user");
				HashMap<String, Integer> cartMap = current.getCart();
				for(String key: cartMap.keySet()) {
					String query2 = "insert into sales (customerId, movieId, saleDate) values (?, ?, ?)";
					
					PreparedStatement statement2 = dbcon.prepareStatement(query2);
					
					statement2.setString(1, current.getId());
					statement2.setString(2, key);
					statement2.setString(3, LocalDate.now().toString());
					
					statement2.executeUpdate();
					
					statement2.close();
				}
			}
			
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
