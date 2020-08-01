

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CartSubtractorServlet
 */
@WebServlet(name = "/CartSubtractorServlet", urlPatterns = "/api/subtractor")
public class CartSubtractorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User current = (User)request.getSession().getAttribute("user");
		current.add(request.getParameter("movie_id"));
		request.getSession().setAttribute("user", current);
		response.setStatus(200);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User current = (User) request.getSession().getAttribute("user");
		current.subtract(request.getParameter("movie_id"));
		request.getSession().setAttribute("user", current);
		response.setStatus(200);
	}
}
