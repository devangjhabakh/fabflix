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
 * Servlet implementation class TableMetadataServlet
 */
@WebServlet(name = "/TableMetadataServlet", urlPatterns = "/api/table_metadata")
public class TableMetadataServlet extends HttpServlet {
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
			
			String query = "show tables";
			
			Statement statement = dbcon.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			JsonArray jsonArray = new JsonArray();
			
			while(rs.next()) {
				JsonArray jsonArray2 = new JsonArray();
				
				String tablename = rs.getString("Tables_in_moviedb");
				String tableQuery = "select column_name as attribute, data_type as type from information_schema.columns where table_schema = 'moviedb' and table_name = ?";
				
				PreparedStatement tableStatement = dbcon.prepareStatement(tableQuery);
				tableStatement.setString(1, tablename);
				
				ResultSet rs2 = tableStatement.executeQuery();
				
				while(rs2.next()) {
					JsonObject jsonObject = new JsonObject();
					
					jsonObject.addProperty("attribute", rs2.getString("attribute"));
					jsonObject.addProperty("type", rs2.getString("type"));
					
					jsonArray2.add(jsonObject);
				}
				
				JsonObject jsonObject3 = new JsonObject();
				
				jsonObject3.addProperty("tablename", tablename);
				jsonObject3.add("schema", jsonArray2);
				
				jsonArray.add(jsonObject3);
				
				tableStatement.close();
			}
			
			out.write(jsonArray.toString());
			
			response.setStatus(200);
			
			rs.close();
			dbcon.close();
			statement.close();
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
