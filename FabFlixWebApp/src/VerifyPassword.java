import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class VerifyPassword {
	public static void main(String[] args) throws Exception {
		System.out.println(verifyCredentials("a@email.com","a2"));
		System.out.println(verifyCredentials("a@email.com","a3"));
	}
	
	public static boolean verifyCredentials(String email, String password) {
		
		String loginuser = "mytestuser";
		String loginpassword = "mypassword";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(loginUrl,loginuser,loginpassword);
			PreparedStatement statement = connection.prepareStatement("select * from customers where email = ?");
			
			statement.setString(1,email);
			
			ResultSet rs = statement.executeQuery();
			
			boolean success = false;
			if(rs.next()) {
				String encryptedPassword = rs.getString("password");
				success = new StrongPasswordEncryptor().checkPassword(password,encryptedPassword);
			}
			
			System.out.println("The password is " + success);
			
			rs.close();
			statement.close();
			connection.close();
			
			return success;
		} catch (Exception e) {
			
			System.out.println(e.toString());
			return false;
		}
	}
}
