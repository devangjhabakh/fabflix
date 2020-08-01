

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CartAdderServlet
 */
@WebServlet(name = "/CartAdderServlet", urlPatterns = "/api/adder")
public class CartAdderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User current = (User)request.getSession().getAttribute("user");
		current.add(request.getParameter("movie_id"));
		request.getSession().setAttribute("user", current);
		response.setStatus(200);
	}
}
